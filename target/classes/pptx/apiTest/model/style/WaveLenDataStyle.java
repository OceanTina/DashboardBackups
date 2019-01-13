package pptx.apiTest.model.style;

import java.awt.*;

public class WaveLenDataStyle {
    //波道占用情况的颜色
    private Color[] channelColors;
    //序号列宽
    private int indexColLength = 20;
    //源宿站点列宽
    private int siteColLength = 70;

    public Color[] getChannelColors() {
        return channelColors;
    }

    public void setChannelColors(Color[] channelColors) {
        this.channelColors = channelColors;
    }

    public int getIndexColLength() {
        return indexColLength;
    }

    public void setIndexColLength(int indexColLength) {
        this.indexColLength = indexColLength;
    }

    public int getSiteColLength() {
        return siteColLength;
    }

    public void setSiteColLength(int siteColLength) {
        this.siteColLength = siteColLength;
    }
}
