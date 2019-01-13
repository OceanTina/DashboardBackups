package source;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Task {
    int taskTpe();

    //任务组
    String group() default "default";

    //任务执行方式，串行还是并行
    EnumTaskMode mode() default EnumTaskMode.TASK_MODE_PARALLEL;

    //事件ID
    String event() default "";
}
