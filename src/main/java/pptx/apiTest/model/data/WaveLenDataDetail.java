package pptx.apiTest.model.data;

public class WaveLenDataDetail {
    private int index;
    private String srcSite;
    private String snkSite;
    //0对应空闲1,2,3,4,5对应颜色为WaveLenDataStyle中的channelColors
    private int [] channels;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSrcSite() {
        return srcSite;
    }

    public void setSrcSite(String srcSite) {
        this.srcSite = srcSite;
    }

    public String getSnkSite() {
        return snkSite;
    }

    public void setSnkSite(String snkSite) {
        this.snkSite = snkSite;
    }

    public int[] getChannels() {
        return channels;
    }

    public void setChannels(int[] channels) {
        this.channels = channels;
    }
}
