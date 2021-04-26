package cn.pioneeruniverse.common.utils;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.dev.entity.VerificationResult;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.StringReader;
import java.util.*;

public class SqlVerificationUtil extends BaseController{

	private static Logger log = LoggerFactory.getLogger(SqlVerificationUtil.class);

	public List<VerificationResult> toConstruction(String sqlText){
		List<VerificationResult> resultList = checkSql(sqlText);
		return resultList;
	}

	private static List<VerificationResult> checkSql(String sqlText)  {
		List<VerificationResult> resultList = new ArrayList<>();
		try{
			Statements statements = CCJSqlParserUtil.parseStatements(sqlText);
			List<Statement> list = statements.getStatements();
			for (int i = 0; i < list.size(); i++) {
				String message = "";
				VerificationResult verification = new VerificationResult();
				Statement statement = CCJSqlParserUtil.parse(new StringReader(list.get(i).toString()));
				verification.setSql(statement.toString());

				if (statement instanceof Alter) {
					Alter alterStatement = (Alter) statement;
					Table table = alterStatement.getTable();
					if(table.getSchemaName()==null){
						message = message + "表"+table+"：未指定SchemaName。\n";
					}
				} else if (statement instanceof CreateIndex) {
					CreateIndex createIndexStatement = (CreateIndex) statement;
					Table table = createIndexStatement.getTable();
					if(table.getSchemaName()==null){
						message = message + "表"+table+"：未指定SchemaName。\n";
					}
				} else if (statement instanceof CreateTable) {
					CreateTable createTableStatement = (CreateTable) statement;
					Table table = createTableStatement.getTable();
					if(table.getSchemaName()==null){
						message = message + "表"+table+"：未指定SchemaName。\n";
					}
				} else if (statement instanceof CreateView) {
					CreateView createViewStatement = (CreateView) statement;
					Table table = createViewStatement.getView();
					if(table.getSchemaName()==null){
						message = message + "视图"+table+"：未指定SchemaName。\n";
					}
					Select selectStatement = createViewStatement.getSelect();
					TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
					List<String> listStr = tablesNamesFinder.getTableList(selectStatement);
					for (String str : listStr){
						boolean status = str.contains(".");
						if(status == false){
							message = message + "表"+str+"：未指定SchemaName。\n";
						}
					}
				} else if (statement instanceof Delete) {
					Delete deleteStatement = (Delete) statement;
					Expression expression = deleteStatement.getWhere();
					TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
					List<String> listStr = tablesNamesFinder.getTableList(deleteStatement);
					for (String str : listStr){
						boolean status = str.contains(".");
						if(status == false){
							message = message + "表"+str+"：未指定SchemaName。\n";
						}
					}
					if (expression == null) {
						message = message + "Delete语句：" + "未检测到where条件。";
					}
				} else if (statement instanceof Drop) {
					Drop dropTableStatement = (Drop) statement;
					Table table = dropTableStatement.getName();
					if(table.getSchemaName()==null){
						message = message + "表"+table+"：未指定SchemaName。\n";
					}
				} else if (statement instanceof Execute) {
					Execute executeStatement = (Execute) statement;
					String name = executeStatement.getName();
					boolean status = name.contains(".");
					if(status == false){
						message = message + "函数"+name+"：未指定SchemaName。\n";
					}
				} else if (statement instanceof Insert) {
					Insert insertStatement = (Insert) statement;
					TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
					List<String> listStr = tablesNamesFinder.getTableList(insertStatement);
					for (String str : listStr){
						boolean status = str.contains(".");
						if(status == false){
							message = message + "表"+str+"：未指定SchemaName。\n";
						}
					}
				} else if (statement instanceof Merge) {
					Merge mergeStatement = (Merge) statement;
					Table table = mergeStatement.getTable();
					Table usingTable = mergeStatement.getUsingTable();
					if(table.getSchemaName()==null){
						message = message + "表" + table+"：未指定SchemaName。\n";
					}
					if(usingTable.getSchemaName()==null){
						message = message + "表" + usingTable+"：未指定SchemaName。\n";
					}
				} else if (statement instanceof Replace) {
					Replace replaceStatement = (Replace) statement;
					Table table = replaceStatement.getTable();
					if(table.getSchemaName()==null){
						message = message + "表" + table+"：未指定SchemaName。\n";
					}
				} else if (statement instanceof Select) {
					Select selectStatement = (Select) statement;
					TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
					List<String> listStr = tablesNamesFinder.getTableList(selectStatement);
				} else if (statement instanceof Truncate) {
					Truncate truncateStatement = (Truncate) statement;
					Table table = truncateStatement.getTable();
					if(table.getSchemaName()==null){
						message = message + "表"+table+"：未指定SchemaName。\n";
					}
				} else if (statement instanceof Update) {
					Update updateStatement = (Update) statement;
					Expression expression = updateStatement.getWhere();
					TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
					List<String> listStr = tablesNamesFinder.getTableList(updateStatement);
					for (String str : listStr){
						boolean status = str.contains(".");
						if(status == false){
							message = message + "表"+str+"：未指定SchemaName。\n";
						}
					}
					if (expression == null) {
						message = message + "update语句：" + "未检测到where条件。";
					}
				} else if (statement instanceof Upsert) {
					Upsert upsertStatement = (Upsert) statement;
					Table table = upsertStatement.getTable();
					if(table.getSchemaName()==null){
						message = message +"表"+table+"：未指定SchemaName。\n";
					}
				} else {
					message = message +"未检测出操作方式\n";
				}
				if(!"".equals(message)){
					verification.setMessage(message);
					verification.setStatus(2);
					resultList.add(verification);
				}
			}
		}catch (JSQLParserException e){
			System.out.println("sql语法错误，请检查sql之间是否未加分号");
			e.printStackTrace();
		}
		return resultList;
	}

	public static void checkSql2(String sql,String dbType) {
		try{
			// 新建 MySQL Parser
			if("".equals(dbType)){
				dbType = JdbcConstants.MYSQL;
			}
			//SQLStatementParser parser = new MySqlStatementParser(sql);
			SQLStatementParser parser = new SQLStatementParser(sql,dbType);

			// 使用Parser解析生成AST，这里SQLStatement就是AST
			SQLStatement sqlStatement = parser.parseStatement();
			//SQLStatement sqlStatement = parser.parseStatement(true);

			MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
			sqlStatement.accept(visitor);

			log.info("getTables:{}" , visitor.getTables());
			log.info("getParameters:{}" ,visitor.getParameters());
			log.info("getConditions:{}" ,visitor.getConditions());
			log.info("getOrderByColumns:{}" , visitor.getOrderByColumns());
			log.info("getGroupByColumns:{}" , visitor.getGroupByColumns());
			log.info("getFunctions:{}",visitor.getFunctions());

			Map<TableStat.Name, TableStat> tables = visitor.getTables();
			for(Map.Entry<TableStat.Name, TableStat> vo : tables.entrySet()){
				String key =vo.getKey().toString();
				String value =vo.getValue().toString().toUpperCase(Locale.ENGLISH);

			}
		}catch (Exception e) {

		}
	}
}
