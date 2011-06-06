package com.donkfish.tools.client.tools.results;

import com.donkfish.core.client.widget.clipboard.ZeroClipboard;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ResultsPanel extends SimplePanel {

    FlexTable flexTable = new FlexTable();
    ZeroClipboard json = new ZeroClipboard("JSON");
    ZeroClipboard xml = new ZeroClipboard("XML");
    ZeroClipboard text = new ZeroClipboard("Text");

    ArrayList<String> labels = new ArrayList<String>();
    HashMap<String, String> values = new HashMap<String, String>();

    public ResultsPanel() {
        super();        
        add(flexTable);
        flexTable.setWidth("100%");
        Element caption = DOM.createCaption();
        DOM.setInnerText(caption, "Results");
        DOM.appendChild(flexTable.getElement(), caption);


        text.getElement().getStyle().setFloat(Style.Float.RIGHT);
        json.getElement().getStyle().setFloat(Style.Float.RIGHT);
        xml.getElement().getStyle().setFloat(Style.Float.RIGHT);

        json.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
        xml.getElement().getStyle().setMarginRight(5, Style.Unit.PX);

        flexTable.setWidget(0, 0, text);
        flexTable.setWidget(1, 0, json);
        flexTable.setWidget(2, 0, xml);

        DOM.appendChild(caption, text.getElement());
        DOM.appendChild(caption, json.getElement());
        DOM.appendChild(caption, xml.getElement());

        addStyleName(TableStyles.RES.styles().mainTableStyle());
        flexTable.getColumnFormatter().setWidth(0, "80%");
    }

    public void clearLoading() {
    }

    public void showLoading() {
    }

    public void putValue(String label, int value) {

        if(!labels.contains(label))
            labels.add(label);

        int rowNumber = labels.indexOf(label);

        values.put(label, value + "");

        flexTable.setWidget(rowNumber,0, new HTML(label));
        flexTable.setWidget(rowNumber,1, new HTML(value + ""));

        if(rowNumber % 2 == 0)
        {
            flexTable.getRowFormatter().setStyleName(rowNumber, TableStyles.RES.styles().even());
        }
        else
        {
            flexTable.getRowFormatter().setStyleName(rowNumber, TableStyles.RES.styles().odd());
        }

        refreshResultsPanel();
    }

    private void refreshResultsPanel() {

        refreshText();
        refreshXML();
        refreshJSON();
    }

    private void refreshText() {
        String value = "";

        for(String label : labels)
        {
            value += label + ", " + values.get(label) + "\n";
        }

        text.setText(value);
    }

    private void refreshXML() {
        String value = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<rows>\n";

        for(String label : labels)
        {
            value += "<row><label>" + label + "</label><value>" + values.get(label) + "</value></row>\n";
        }

        value += "</rows>";
        xml.setText(value);
    }

    private void refreshJSON() {
        String value = "{\n";

        for(String label : labels)
        {
            value += "\t\"" + label + "\": \"" + values.get(label) + "\"\n";
        }

        value += "}";

        json.setText(value);
    }

    static interface Styles extends CssResource {

    }

    static interface Resources extends ClientBundle {
        @Source("ResultsPanel.css")
        Styles styles();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }
}
