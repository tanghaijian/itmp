package cn.pioneeruniverse.common.sonar.bean;
/**
 *
 * @ClassName:Component
 * @deprecated
 * @author author
 * @date 2020年8月19日
 *
 */
public class Component {

    private String organization;
    private String id;
    private String visibility;
    private String lastAnalysisDate;
    private String key;
    private boolean enabled;
    private String qualifier;
    private String name;
    private String longName;
    private String path;

    public String getOrganization() {
        return organization;
    }

    public String getId() {
        return id;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getLastAnalysisDate() {
        return lastAnalysisDate;
    }

    public String getKey() {
        return key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getName() {
        return name;
    }

    public String getLongName() {
        return longName;
    }

    public String getPath() {
        return path;
    }
}
