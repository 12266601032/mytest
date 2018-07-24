package com.example.jsr303demo.model;

/**
 * 信息码
 *
 * @date 2018/3/23
 */
public interface CodeMessage<C> {
    /**
     * 获取信息码
     *
     * @return 信息码
     */
    C getCode();

    /**
     * 获取信息
     *
     * @return
     */
    String getMessage();


    /**
     * 是否成功
     * @return
     */
    boolean isSuccess();

}
