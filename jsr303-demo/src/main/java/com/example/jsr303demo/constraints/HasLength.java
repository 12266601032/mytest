package com.example.jsr303demo.constraints;

import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 这种拓展方法是基于已有的标准注解
 * 因此无需指定@Constraint(validatedBy = { })
 * 而加上该注解的作用是，让验证器实现将该注解作为一个标准约束来解析，而不是作为普通注解给忽略掉
 *
 * 注解解析实现将约束作为一个树结构来解析，
 * 比如约束注解A上面有约束注解B与C，约束注解C上面又有约束D与E
 * 那么形成的树为：
 * ConstraintTree
 *       A
 *       |
 *    -------
 *   |      |
 *   B      C
 *        -----
 *       |    |
 *       D    E
 * 这样从A开始递归，最终将每一个节点的约束都解析并收集违反约束的情况。
 * @ReportAsSingleViolation注解的作用是：
 * 因为每一个标准约束都会有自己的message可以输出，如果不使用@ReportAsSingleViolation的话
 * 会产生多个每个违反约束的信息输出，而使用了该注解可以只输出A上面定义的message
 *
 * 其中message、groups、payload 为必须属性
 *
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@NotNull
@Length
public @interface HasLength {

    /**
     * 关于message的语法
     * {}这种为hibernate validator中支持的参数化表达式
     * 其中可选参数为当前注解中的所有以方法名为key的参数
     * 比如这里可以定义信息为：
     * "字符串的长度不能小于{min}。"
     * 最终会转换为(根据min实际值替换):
     * "字符串的长度不能小于1。"
     * @return
     */
    String message() default "{com.example.jsr303demo.constraints.HasLength}";

    /**
     * groups 为分组验证，不配置为Default组，
     * 这样可以根据不同的应用场景来定义一些group
     * 比如在执行save与update操作时，可能很多参数在update时就不是必须的了，
     * 这样就可以定义两个接口，分别表示Save.class组与Update.class组，
     * 在save操作时，使用@valid(group={Save.class})，这样就可以只对在Save.class标记的注解中进行约束验证
     * @return
     */
    Class<?>[] groups() default { };

    /**
     * payload这个参数
     * 应用并不多，可以通过它 来携带给验证器一些元数据信息
     * 比如自定义验证器时，验证对象可以是String、也可以是Optional<String>
     * 这时仅仅只用@NotNull 就无法正确验证了，这时候可以通过payload来标记一些需要特殊处理的操作
     * @return
     */
    Class<? extends Payload>[] payload() default { };

    /**
     * @OverridesAttribute注解表示所有该注解下的组合注解都共享这一个参数
     * 类似于spring中的@AliasFor
     * @return
     */
    @OverridesAttribute(constraint = Length.class, name = "min") int min() default 1;

    @OverridesAttribute(constraint = Length.class, name = "max") int max() default Integer.MAX_VALUE;
}
