package enumeration;

public enum EnumTaskMode {
    /*任务串行执行，相互阻塞 */
    TASK_MODE_SERIAL(0x01),
    /*任务并行执行 */
    TASK_MODE_PARALLEL(0x02),

    TASK_MODE_SERIAL_ON_SAME_TYPE(0x04);

    public int mode;

    private EnumTaskMode(int mode)
    {
        this.mode = mode;
    }
}
