package base;

import java.io.Serializable;

/**
 *
 * @author ruiying.hry
 * @version $Id: Result.java, v 0.1 2017-11-15 下午9:56 ruiying.hry Exp $$
 */
public class Result<T> implements Serializable, Cloneable {

    /** serialVersionUID */
    private static final long serialVersionUID = -8401438341129285558L;

    private boolean           success          = true;

    /** 返回对象 */
    private T                 result;

    /**
     * Getter method for property   result.
     *
     * @return property value of result
     */
    public T getResult() {
        return result;
    }

    /**
     * Setter method for property   result .
     *
     * @param result value to be assigned to property result
     */
    public void setResult(T result) {
        this.result = result;
    }

    /**
     * Setter method for property   success .
     *
     * @param success value to be assigned to property success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}