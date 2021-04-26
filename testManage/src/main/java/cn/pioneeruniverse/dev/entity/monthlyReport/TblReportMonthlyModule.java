package cn.pioneeruniverse.dev.entity.monthlyReport;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 话术模板表
 * Created by aviyy on 2020/10/21.
 */
public class TblReportMonthlyModule  extends BaseEntity {

    private String yearMonth; //时间

    private int page;//页码

    private int area;//区域

    private int num;//列表序号

    private String content;//模板内容{{code}}

    @TableField(exist = false)
    private String contentName;//模板内容{{名称}}

    @TableField(exist = false)
    private String contentValue;//数据替换后的模板内容，即需要显示的内容

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentValue() {
        return contentValue;
    }

    public void setContentValue(String contentValue) {
        this.contentValue = contentValue;
    }
}
