package cn.pioneeruniverse.project.common;

import cn.pioneeruniverse.common.constants.Constants;
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

public class arrMaxMerge {

    private static Logger log = LoggerFactory.getLogger(arrMaxMerge.class);
    public static void main(String args[]){

        Integer a= new Integer(2);
        if(a.equals(2)){
            System.out.println(true);
        }else {
            System.out.println(false);
        }
        String sqlText ="\n" +
                "SET FOREIGN_KEY_CHECKS = 0;\n" +
                "\n" +
                "DROP TABLE IF EXISTS `message_log`;\n" +
                "CREATE TABLE `message_log`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `message_data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '????????????',\n" +
                "  `message_type` tinyint(2) NULL DEFAULT NULL COMMENT '????????????1??????0??????',\n" +
                "  `message_target` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '???????????????',\n" +
                "  `message_statut` tinyint(255) NULL DEFAULT NULL COMMENT '????????????0??????1??????',\n" +
                "  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '????????????',\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;\n" +
                "\n" +
                "SET FOREIGN_KEY_CHECKS = 1;\n" +
                "\n" +
                "update cby.tbl_user_info set user_name = 'aaa'  id = 1 and username = 'abc';\n" +
                "select IFNULL(A,0) from cby.tbl_user_info user left join (select * from cby.tbl_notice_info notice where id = 2 ) n on n.user_id = user.id   where user.id = 1 and user.bc like '%test_%';";

        String sql ="/*\n" +
                " Navicat MySQL Data Transfer\n" +
                "\n" +
                " Source Server         : itzhgldb\n" +
                " Source Server Type    : MySQL\n" +
                " Source Server Version : 50722\n" +
                " Source Host           : 10.1.11.128:3306\n" +
                " Source Schema         : itzhgldb\n" +
                "\n" +
                " Target Server Type    : MySQL\n" +
                " Target Server Version : 50722\n" +
                " File Encoding         : 65001\n" +
                "\n" +
                " Date: 24/03/2020 10:18:28\n" +
                "*/\n" +
                "\n" +
                "SET NAMES utf8mb4;\n" +
                "SET FOREIGN_KEY_CHECKS = 0;\n" +
                "\n" +
                "-- ----------------------------\n" +
                "-- Table structure for tbl_user_info\n" +
                "-- ----------------------------\n" +
                "DROP TABLE IF EXISTS `tbl_user_info`;\n" +
                "CREATE TABLE `tbl_user_info`  (\n" +
                "  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
                "  `USER_ACCOUNT` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '??????????????????',\n" +
                "  `USER_PASSWORD` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '??????????????????',\n" +
                "  `USER_NAME` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????????????',\n" +
                "  `EMPLOYEE_NUMBER` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '?????????',\n" +
                "  `EMAIL` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '??????',\n" +
                "  `GENDER` tinyint(2) NULL DEFAULT NULL COMMENT '??????(1:??????2:???)',\n" +
                "  `BIRTHDAY` date NULL DEFAULT NULL COMMENT '????????????',\n" +
                "  `USER_TYPE` tinyint(2) NULL DEFAULT NULL COMMENT '???????????????1:???????????????2:???????????????',\n" +
                "  `COMPANY_ID` bigint(20) NULL DEFAULT NULL COMMENT '??????',\n" +
                "  `ENTRY_DATE` date NULL DEFAULT NULL COMMENT '????????????',\n" +
                "  `LEAVE_DATE` date NULL DEFAULT NULL COMMENT '????????????',\n" +
                "  `DEPT_ID` int(11) NULL DEFAULT NULL COMMENT '??????ID',\n" +
                "  `USER_STATUS` tinyint(2) NOT NULL COMMENT '???????????????1????????????2????????????',\n" +
                "  `IS_ALLOWED` tinyint(2) NOT NULL DEFAULT 0 COMMENT '???????????????????????????????????????????????????????????????0?????????1?????????',\n" +
                "  `USER_SCM_ACCOUNT` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '????????????????????????',\n" +
                "  `USER_SCM_PASSWORD` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '????????????????????????',\n" +
                "  `STATUS` tinyint(2) NULL DEFAULT 1 COMMENT '???????????? 1=?????????2=??????',\n" +
                "  `CREATE_BY` bigint(20) NULL DEFAULT 1 COMMENT '?????????',\n" +
                "  `CREATE_DATE` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????????????',\n" +
                "  `LAST_UPDATE_BY` bigint(20) NULL DEFAULT NULL COMMENT '???????????????',\n" +
                "  `LAST_UPDATE_DATE` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '??????????????????',\n" +
                "  PRIMARY KEY (`ID`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 1000000 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '???????????????' ROW_FORMAT = Compressed;\n" +
                "\n" +
                "SET FOREIGN_KEY_CHECKS = 1;";
       // String [] sqls = sqlText.split(";");

        /*List<Map<String, Object>> mapList = new ArrayList<>();
        for(String sql : sqls){
            sql = sql.trim();
            Map<String, Object> map = checkSql(sql);
            if(map.size()>0){
                log.info("--------------------------??????-----------------------------");
                log.info(map.get("status").toString());
                log.info(map.get("message").toString());
                log.info("--------------------------??????-----------------------------");
                mapList.add(map);
            }
        }
        for(Map<String, Object> map : mapList ){
            System.out.println(map.get("message").toString());
            System.out.println("\n");
        }*/

        //checkSql3(sql);
    }

    public static Map<String, Object> checkSql(String sql) {
        Map<String, Object> map = new HashMap<>();
        String message = "";
        try{
            // ?????? MySQL Parser
            String dbType = JdbcConstants.MYSQL;

            //SQLStatementParser parser = new MySqlStatementParser(sql);
            SQLStatementParser parser = new SQLStatementParser(sql,dbType);

            // ??????Parser????????????AST?????????SQLStatement??????AST
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
                if("UPDATE".equals(value)||"DELETE".equals(value)){
                    boolean status = key.contains(".");
                    if(status==false){
                        message = message + "SQL???"+sql + "\n???"+key+"????????????????????????";
                        map.put("status", Constants.ITMP_RETURN_FAILURE);
                        map.put("message", message);
                    }
                    if(sql.contains("where")==false){
                        message = message + "SQL???"+sql + "\n???"+key+"???????????????where?????????";
                        map.put("status", Constants.ITMP_RETURN_FAILURE);
                        map.put("message", message);
                    }
                    break;
                }else if("INSERT".equals(value)){
                    boolean status = key.contains(".");
                    if(status==false){
                        message = message + "SQL???"+sql + "\n???"+key+"????????????????????????";
                        map.put("status", Constants.ITMP_RETURN_FAILURE);
                        map.put("message", message);
                    }
                    break;
                }else{
                    break;
                }
            }
        }catch (Exception e){
            message = message + "SQL???"+ sql + "\n???????????????"+e.getMessage();
            map.put("status", Constants.ITMP_RETURN_FAILURE);
            map.put("message", message);
        }
        return map;
    }

    public static void checkSql2(String sql) {
        // ?????? MySQL Parser
        String dbType = JdbcConstants.MYSQL;

        //SQLStatementParser parser = new MySqlStatementParser(sql);
        SQLStatementParser parser = new SQLStatementParser(sql,dbType);

        // ??????Parser????????????AST?????????SQLStatement??????AST
        //SQLStatement sqlStatement = parser.parseStatement();
        SQLStatement sqlStatement = parser.parseStatement(true);

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);

        log.info("getTables:{}" , visitor.getTables());
        log.info("getParameters:{}" ,visitor.getParameters());
        log.info("getConditions:{}" ,visitor.getConditions());
        log.info("getOrderByColumns:{}" , visitor.getOrderByColumns());
        log.info("getGroupByColumns:{}" , visitor.getGroupByColumns());
        log.info("getFunctions:{}",visitor.getFunctions());
    }

    public static void checkSql3(String sql)  {
        try{
            List<Map<String, Object>> mapList = new ArrayList<>();
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            List<Statement> list = statements.getStatements();
            for (int i = 0; i < list.size(); i++) {
                String message = "";
                String operateType = "";
                Map<String, Object> map = new HashMap<>();
                Statement statement = CCJSqlParserUtil.parse(new StringReader(list.get(i).toString()));
                log.info(statement.toString());
                if (statement instanceof Alter) {
                    operateType = "Alter";
                    Alter alterStatement = (Alter) statement;
                    Table table = alterStatement.getTable();
                    if(table.getSchemaName()==null){
                        message = message + "SQL???"+statement + "\n???"+table+"????????????SchemaName???\n";
                    }
                } else if (statement instanceof CreateIndex) {
                    operateType = "CreateIndex";
                    CreateIndex createIndexStatement = (CreateIndex) statement;
                    Table table = createIndexStatement.getTable();
                    if(table.getSchemaName()==null){
                        message = message + "SQL???"+statement + "\n???"+table+"????????????SchemaName???\n";
                    }
                } else if (statement instanceof CreateTable) {
                    operateType = "CreateTable";
                    CreateTable createTableStatement = (CreateTable) statement;
                    Table table = createTableStatement.getTable();
                    if(table.getSchemaName()==null){
                        message = message + "SQL???"+statement + "\n???"+table+"????????????SchemaName???\n";
                    }
                } else if (statement instanceof CreateView) {
                    operateType = "CreateView";
                    CreateView createViewStatement = (CreateView) statement;
                    Table table = createViewStatement.getView();
                    if(table.getSchemaName()==null){
                        message = message + "SQL???"+statement + "\n??????"+table+"????????????SchemaName???\n";
                    }
                    Select selectStatement = createViewStatement.getSelect();
                    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                    List<String> listStr = tablesNamesFinder.getTableList(selectStatement);
                    for (String str : listStr){
                        boolean status = str.contains(".");
                        if(status == false){
                            message = message + "SQL???"+statement + "\n???"+str+"????????????SchemaName???\n";
                        }
                    }
                } else if (statement instanceof Delete) {
                    operateType = "Delete";
                    Delete deleteStatement = (Delete) statement;
                    Expression expression = deleteStatement.getWhere();
                    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                    List<String> listStr = tablesNamesFinder.getTableList(deleteStatement);
                    for (String str : listStr){
                        boolean status = str.contains(".");
                        if(status == false){
                            message = message + "SQL???"+statement + "\n???"+str+"????????????SchemaName???\n";
                        }
                    }
                    if (expression == null) {
                        message = message + "SQL???"+sql + "\n????????????where?????????";
                    }
                } else if (statement instanceof Drop) {
                    operateType = "Drop";
                    Drop dropTableStatement = (Drop) statement;
                    Table table = dropTableStatement.getName();
                    if(table.getSchemaName()==null){
                        message = message + "SQL???"+statement + "\n???"+table+"????????????SchemaName???\n";
                    }
                } else if (statement instanceof Execute) {
                    operateType = "Execute";
                    Execute executeStatement = (Execute) statement;
//					List<Expression> expressions = executeStatement.getExprList().getExpressions();
                    String name = executeStatement.getName();
                    boolean status = name.contains(".");
                    if(status == false){
                        message = message + "SQL???"+statement + "\n??????"+name+"????????????SchemaName???\n";
                    }
                } else if (statement instanceof Insert) {
                    operateType = "Insert";
                    Insert insertStatement = (Insert) statement;
                    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                    List<String> listStr = tablesNamesFinder.getTableList(insertStatement);
                    for (String str : listStr){
                        boolean status = str.contains(".");
                        if(status == false){
                            message = message + "SQL???"+statement + "\n???"+str+"????????????SchemaName???\n";
                        }
                    }
                } else if (statement instanceof Merge) {
                    operateType = "Merge";
                    Merge mergeStatement = (Merge) statement;
                    Table table = mergeStatement.getTable();
                    Table usingTable = mergeStatement.getUsingTable();
                    if(table.getSchemaName()==null){
                        message = message + "SQL???"+statement + "\n???"+table+"????????????SchemaName???\n";
                    }
                    if(usingTable.getSchemaName()==null){
                        message = message + "SQL???"+statement + "\n???"+usingTable+"????????????SchemaName???\n";
                    }
                } else if (statement instanceof Replace) {
                    operateType = "Replace";
                    Replace replaceStatement = (Replace) statement;
                    Table table = replaceStatement.getTable();
                    if(table.getSchemaName()==null){
                        message = message + "SQL???"+statement + "\n???"+table+"????????????SchemaName???\n";
                    }
                } else if (statement instanceof Select) {
                    Select selectStatement = (Select) statement;
                    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                    List<String> listStr = tablesNamesFinder.getTableList(selectStatement);
                    log.info(listStr.toString());
                } else if (statement instanceof Truncate) {
                    operateType = "Truncate";
                    Truncate truncateStatement = (Truncate) statement;
                    Table table = truncateStatement.getTable();
                    if(table.getSchemaName()==null){
                        message = message + "SQL???"+statement + "\n???"+table+"????????????SchemaName???\n";
                    }
                } else if (statement instanceof Update) {
                    operateType = "Update";
                    Update updateStatement = (Update) statement;
                    Expression expression = updateStatement.getWhere();
                    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                    List<String> listStr = tablesNamesFinder.getTableList(updateStatement);
                    for (String str : listStr){
                        boolean status = str.contains(".");
                        if(status == false){
                            message = message + "SQL???"+list.get(i).toString() + "\n???"+str+"????????????SchemaName???\n";
                        }
                    }
                    if (expression == null) {
                        message = message + "SQL???"+sql + "\n????????????where?????????";
                    }
                } else if (statement instanceof Upsert) {
                    operateType = "Upsert";
                    Upsert upsertStatement = (Upsert) statement;
                    Table table = upsertStatement.getTable();
                    if(table.getSchemaName()==null){
                        message = message +"SQL???"+statement + "\n???"+table+"????????????SchemaName???\n";
                    }
                } else {
                    operateType = "NONE";
                    message = message +"SQL???"+statement + "\n????????????????????????\n";
                }
                if(!"".equals(message)){
                    map.put("operateType",operateType);
                    map.put("status", Constants.ITMP_RETURN_FAILURE);
                    map.put("message", message);
                    mapList.add(map);
                }
            }
        }catch (JSQLParserException e){
            e.printStackTrace();
            log.error("sql????????????" + ":" + e.getMessage(), e);
        }
    }
}