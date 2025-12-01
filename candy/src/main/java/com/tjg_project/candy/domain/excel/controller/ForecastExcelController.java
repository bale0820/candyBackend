package com.tjg_project.candy.domain.excel.controller;

import com.tjg_project.candy.domain.excel.dto.ForecastExcelRow;
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
public class ForecastExcelController {

    @PostMapping("/forecast")
    public ResponseEntity<byte[]> exportForecast(@RequestBody ForecastRequest req) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Forecast");

        // -------- Header --------
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("구분");
        header.createCell(1).setCellValue("날짜");
        header.createCell(2).setCellValue("예측값");

        int rowNum = 1;
        for (ForecastExcelRow r : req.getRows()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(r.getType());
            row.createCell(1).setCellValue(r.getDate());
            row.createCell(2).setCellValue(r.getValue());
        }

        // -------- Chart Anchor --------
        XSSFDrawing draw = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = new XSSFClientAnchor(
                0, 0, 0, 0,
                4, 1, 22, 25
        );

        XSSFChart chart = draw.createChart(anchor);
        chart.setTitleText("판매량 예측");

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.RIGHT);

        // -------- Axis 설정 --------
        // 날짜(Y축)
        XDDFCategoryAxis yAxis = chart.createCategoryAxis(AxisPosition.LEFT);
        yAxis.setTitle("날짜");

        // 예측값(X축)
        XDDFValueAxis xAxis = chart.createValueAxis(AxisPosition.BOTTOM);
        xAxis.setTitle("예측값");

        // -------- DataRange --------
        var dates = XDDFDataSourcesFactory.fromStringCellRange(
                sheet, new CellRangeAddress(1, rowNum - 1, 1, 1)
        );

        var values = XDDFDataSourcesFactory.fromNumericCellRange(
                sheet, new CellRangeAddress(1, rowNum - 1, 2, 2)
        );

        // -------- Bar Chart --------
        XDDFBarChartData barData =
                (XDDFBarChartData) chart.createData(ChartTypes.BAR, yAxis, xAxis);

        // ⭐ 바 차트를 가로형으로 설정
        barData.setBarDirection(BarDirection.BAR);

        // -------- Series --------
        XDDFBarChartData.Series valueSeries =
                (XDDFBarChartData.Series) barData.addSeries(dates, values);

        valueSeries.setTitle("예측값", null);

        // 색 (파란색)
        valueSeries.setFillProperties(
                new XDDFSolidFillProperties(XDDFColor.from(PresetColor.BLUE))
        );

        chart.plot(barData);

        // -------- Output --------
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=forecast.xlsx")
                .body(bos.toByteArray());
    }

    @Data
    static class ForecastRequest {
        private List<ForecastExcelRow> rows;
    }
}
