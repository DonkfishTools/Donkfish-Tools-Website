package com.donkfish.tools.client.tools.html;

import com.donkfish.core.client.prettydiff.PrettyDiff;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;

public class TableGenerator extends Composite {
    VerticalPanel mainPanel = new VerticalPanel();
    private int width;
    private int height;

    public TableGenerator() {
        mainPanel.add(getTableRowColumnWidget());
//        mainPanel.add(getTableOptionsWidget());
        mainPanel.add(getTableHtmlWidget());
//        mainPanel.add(getTablePreviewWidget());
        initWidget(mainPanel);
    }

    private Widget getTablePreviewWidget() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    TextArea textArea = new TextArea();
    private Widget getTableHtmlWidget() {

        textArea.setHeight("300px");
        textArea.setWidth("300px");
        return textArea;
    }

    private Widget getTableOptionsWidget() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    Grid grid;
    private Widget getTableRowColumnWidget() {
        int row = 15;
        int columns = 10;

        grid = new Grid(row,columns);

        for(int r = 0; r < row; r++)
        {
            for(int c = 0; c < columns;c++)
            {
                final int width = c;
                final int height = r;
                final FocusPanel focusPanel = new FocusPanel();
                focusPanel.setWidth("25px");
                focusPanel.setHeight("25px");
                focusPanel.getElement().getStyle().setBackgroundColor("black");
                focusPanel.addMouseOverHandler(new MouseOverHandler() {
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        updateTableSize(width, height);
                        focusPanel.getElement().getStyle().setBackgroundColor("blue");
                    }
                });
                grid.setWidget(r, c, focusPanel);
            }
        }



        return grid;
    }

    private void updateTableSize(int width, int height) {

        this.width = width;
        this.height = height;
        refresh();
    }

    private void refresh() {
        for(int r = 0; r < grid.getRowCount(); r++)
        {
            for(int c = 0; c < grid.getColumnCount();c++)
            {
                Widget panel = grid.getWidget(r, c);
                if(r <= height && c <= width)
                    panel.getElement().getStyle().setBackgroundColor("blue");
                else
                    panel.getElement().getStyle().setBackgroundColor("black");
            }
        }

        Grid tempGrid = new Grid(height, width);

        textArea.setText(PrettyDiff.pretty(tempGrid.getElement().getString()));

    }

    static interface Styles extends CssResource {

    }

    static interface Resources extends ClientBundle {
        @Source("TableGenerator.css")
        Styles styles();

    }

    public static Resources RES;

    static {
        RES = (Resources) GWT.create(Resources.class);
        RES.styles().ensureInjected();
    }

}