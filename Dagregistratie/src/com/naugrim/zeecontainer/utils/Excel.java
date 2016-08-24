package com.naugrim.zeecontainer.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

public class Excel {

	@SuppressWarnings("unchecked")
	public Excel(Map<String, Object[]> data, String string, String[] HeaderValues) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Data");

		Set<String> keyset = data.keySet();

		List<Integer> list = new ArrayList();
		for (String key : keyset) {
			Integer i = Integer.parseInt(key);
			list.add(i);
		}
		System.out.println("Before Sort:");
		Collections.sort(list, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return o2.compareTo(o1);
			}
		});
		List<String> l = new ArrayList<>();
		for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
			Integer object = (Integer) iterator.next();
			l.add(object.toString());
		}
		keyset = new HashSet<>(l);
		int rownum = 0;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}
		sheet.shiftRows(0, sheet.getLastRowNum() + 1, 1, true, true);
		HSSFRow firstrow = sheet.getRow(0);
		for (int i = 0; i < HeaderValues.length; i++) {
			firstrow.createCell(i).setCellValue(HeaderValues[i]);
		}
		CellStyle style = null;

		HSSFFont defaultFont = workbook.createFont();
		defaultFont.setFontHeightInPoints((short) 10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		defaultFont.setItalic(false);

		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setItalic(false);

		style = firstrow.getRowStyle();
		style.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(string));
			workbook.write(out);
			out.close();
			System.out.println("data.xls written successfully on disk.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
