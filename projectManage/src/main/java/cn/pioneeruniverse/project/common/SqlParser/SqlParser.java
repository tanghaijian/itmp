package cn.pioneeruniverse.project.common.SqlParser;


import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: LN  create: 2019-05-06 08:58
 */
public class SqlParser {
    // 查询字段
    private static List<String> test_select_items(String sql)
            throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        List<SelectItem> selectitems = plain.getSelectItems();
        List<String> str_items = new ArrayList<String>();
        if (selectitems != null) {
            for (SelectItem selectitem : selectitems) {
                str_items.add(selectitem.toString());
            }
        }
        return str_items;
    }

    //查询表名 table
    private static List<String> test_select_table(String sql)
            throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        //  System.out.println(statement.toString());

        Select selectStatement = (Select) statement;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        return tablesNamesFinder
                .getTableList(selectStatement);
    }

    //查询 join
    private static List<String> test_select_join(String sql)
            throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;

        PlainSelect plain = (PlainSelect) selectStatement.getSelectBody();
        //System.out.println(plain.toString());
        List<Join> joinList = plain.getJoins();
        List<String> tablewithjoin = new ArrayList<String>();
        if (joinList != null) {
            for (Join join : joinList) {
                join.setLeft(false);
                tablewithjoin.add(join.toString());
                //注意 ， leftjoin rightjoin 等等的to string()区别
            }
        }
        return tablewithjoin;
    }

    // 查询 where
    private static String test_select_where(String sql)
            throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        Expression where_expression = plain.getWhere();
        return where_expression.toString();
    }

    // 查询 group by
    private static List<String> test_select_groupby(String sql)
            throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        GroupByElement groupByElement  = plain.getGroupBy();
        List<Expression> expression = groupByElement.getGroupByExpressions();
        List<String> str_groupby = new ArrayList<String>();
        if (expression != null) {
            for (Expression groupByColumnReference : expression) {
                str_groupby.add(groupByColumnReference.toString());
            }
        }
        return str_groupby;
    }

    // 查询 order by
    private static List<String> test_select_orderby(String sql)
            throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        List<OrderByElement> OrderByElements = plain.getOrderByElements();
        List<String> str_orderby = new ArrayList<String>();
        if (OrderByElements != null) {
            for (OrderByElement orderByElement : OrderByElements) {
                str_orderby.add(orderByElement.toString());
            }
        }
        return str_orderby;
    }

    /**
     * 子查询
     */
    private static Map test_select_subselect(SelectBody selectBody) throws JSQLParserException {
        Map<String, String> map = new HashMap<String, String>();

        if (selectBody instanceof PlainSelect) {
            List<SelectItem> selectItems = ((PlainSelect) selectBody).getSelectItems();
            for (SelectItem selectItem : selectItems) {
                if (selectItem.toString().contains("(") && selectItem.toString().contains(")")) {
                    map.put("selectItemsSubselect", selectItem.toString());
                }
            }

            Expression where = ((PlainSelect) selectBody).getWhere();
            String whereStr = where.toString();
            if (whereStr.contains("(") && whereStr.contains(")")) {
                int firstIndex = whereStr.indexOf("(");
                int lastIndex = whereStr.lastIndexOf(")");
                CharSequence charSequence = whereStr.subSequence(firstIndex, lastIndex + 1);
                map.put("whereSubselect", charSequence.toString());
            }

            FromItem fromItem = ((PlainSelect) selectBody).getFromItem();
//            System.out.println("111----"+((PlainSelect) selectBody).getFromItem());
//            System.out.println(fromItem);
            if (fromItem instanceof SubSelect) {
                map.put("fromItemSubselect", fromItem.toString());
            }

        } else if (selectBody instanceof WithItem) {
            SqlParser.test_select_subselect(((WithItem) selectBody).getSelectBody());
        }
        return map;
    }

    public static void main(String[] args) throws JSQLParserException {

        String sql = "select a.* from emp a,empb b where a.deptno=b.deptno and a.sal=b.sal;";
        List<String> selectTable = test_select_table(sql);
        System.out.println("1----查询的表名：" + selectTable);

        sql = "select age,sex,(select dno from employee where salary >=5000) from dept where did in(select dno from employee where salary >=5000);";
        List<String> selectItems = test_select_items(sql);
        System.out.println("2----查询的字段：" + selectItems);

        sql = "select age,sex,(select dno from employee where salary >=5000) from (select dno from employee where salary >=5000) a where did in(select dno from employee where salary >=5000);";
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select selectStatement = (Select) parserManager.parse(new StringReader(sql));
        //  System.out.println(selectStatement.getSelectBody());
        Map map = test_select_subselect(selectStatement.getSelectBody());
        System.out.println("3----子查询：" + map.toString());

        sql = "select age,sex,name from dept where did in(select dno from employee where salary >=5000);";
        String selectWhere = test_select_where(sql);
        System.out.println("4----where条件：" + selectWhere);

        sql = "select * from student group by id;";
        List<String> selectGroupby = test_select_groupby(sql);
        System.out.println("5----groupby条件:" + selectGroupby);

        sql = "SELECT column_name(s)\n" +
                "FROM table_name1\n" +
                "INNER JOIN table_name2 \n" +
                "ON table_name1.column_name=table_name2.column_name;";
        List<String> selectJoin = test_select_join(sql);
        System.out.println("6----join查询：" + selectJoin);

        sql = "select * from student s inner join teacher t on s.id=t.id order by id;";
        List<String> selectOrderby = test_select_orderby(sql);
        System.out.println("7----orderby条件：" + selectOrderby);

    }

}