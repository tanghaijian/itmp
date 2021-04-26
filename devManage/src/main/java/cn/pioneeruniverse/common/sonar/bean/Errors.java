package cn.pioneeruniverse.common.sonar.bean;

import java.util.List;

/**
 *
 * @ClassName:Errors
 * @deprecated
 * @author author
 * @date 2020年8月24日
 *
 */
public class Errors {

    List<Error> errors;

    public List<Error> getErrors() {
        return errors;
    }

    public class Error {

        String msg;

        public String getMsg() {
            return msg;
        }
    }
}
