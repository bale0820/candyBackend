package com.tjg_project.candy.domain.excel.controller;

import com.tjg_project.candy.domain.excel.dto.ConversionExcelRow;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ConversionExcelController {

    @PostMapping("/conversion")
    public ResponseEntity<byte[]> exportConversion(@RequestBody ConversionRequest req) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Conversion");

        // ---------------- Header ----------------
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("상품명");
        header.createCell(1).setCellValue("클릭수");
        header.createCell(2).setCellValue("구매수");
        header.createCell(3).setCellValue("전환율 (%)");

        // ---------------- Body Rows ----------------
        int rowNum = 1;
        for (ConversionExcelRow r : req.getRows()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(r.getProductName());
            row.createCell(1).setCellValue(r.getClicks());
            row.createCell(2).setCellValue(r.getOrders());
            row.createCell(3).setCellValue(r.getConversionRate());
        }

        // ---------------- Create Chart ----------------
        XSSFDrawing draw = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = new XSSFClientAnchor(
                0, 0, 0, 0,
                5, 1, 22, 25
        );

        XSSFChart chart = draw.createChart(anchor);
        chart.setTitleText("클릭 대비 구매 전환율");

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);

        // ---------------- Axis ----------------
        XDDFCategoryAxis axisY = chart.createCategoryAxis(AxisPosition.LEFT);  // 상품명(Y축)
        XDDFValueAxis axisX = chart.createValueAxis(AxisPosition.BOTTOM);     // 값(X축)

        // ---------------- Data Range ----------------
        var names = XDDFDataSourcesFactory.fromStringCellRange(
                sheet, new CellRangeAddress(1, rowNum - 1, 0, 0)
        );
        var clicks = XDDFDataSourcesFactory.fromNumericCellRange(
                sheet, new CellRangeAddress(1, rowNum - 1, 1, 1)
        );
        var orders = XDDFDataSourcesFactory.fromNumericCellRange(
                sheet, new CellRangeAddress(1, rowNum - 1, 2, 2)
        );

        // ---------------- Bar Chart Data ----------------
        XDDFBarChartData barData =
                (XDDFBarChartData) chart.createData(ChartTypes.BAR, axisY, axisX);

        // ⭐ 가로형(bar) 적용 (중요!!)
        barData.setBarDirection(BarDirection.BAR);

        // ⭐ 클릭수 (파란색)
        XDDFBarChartData.Series clicksSeries =
                (XDDFBarChartData.Series) barData.addSeries(names, clicks);
        clicksSeries.setTitle("클릭수", null);
        clicksSeries.setFillProperties(
                new XDDFSolidFillProperties(XDDFColor.from(PresetColor.BLUE))
        );

        // ⭐ 구매수 (주황색)
        XDDFBarChartData.Series ordersSeries =
                (XDDFBarChartData.Series) barData.addSeries(names, orders);
        ordersSeries.setTitle("구매수", null);
        ordersSeries.setFillProperties(
                new XDDFSolidFillProperties(XDDFColor.from(PresetColor.ORANGE))
        );

        chart.plot(barData);

        // ---------------- Output Stream ----------------
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=conversion.xlsx")
                .body(bos.toByteArray());
    }

    @Data
    static class ConversionRequest {
        private List<ConversionExcelRow> rows;
    }
}
