package pptx.apiTest.model.data;

import pptx.apiTest.model.style.WaveLenDataStyle;

import java.util.List;

public class WaveLenData {
    private WaveLenDataStyle style = new WaveLenDataStyle();
    private List<String> frequencyList;
    private List<WaveLenDataDetail> detailList;
    private String title;

    public WaveLenData(List<String> frequencyList, List<WaveLenDataDetail> detailList) {
        this.frequencyList = frequencyList;
        this.detailList = detailList;
    }

    public WaveLenDataStyle getStyle() {
        return style;
    }

    public void setStyle(WaveLenDataStyle style) {
        this.style = style;
    }

    public List<String> getFrequencyList() {
        return frequencyList;
    }

    public void setFrequencyList(List<String> frequencyList) {
        this.frequencyList = frequencyList;
    }

    public List<WaveLenDataDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<WaveLenDataDetail> detailList) {
        this.detailList = detailList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
