<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.io.*" %>
<%@ page import="com.ibleaders.ibsheet7.ibsheet.text.LoadText" %>
<%
	LoadText ibText = new LoadText(request, response);
    //====================================================================================================
    // [ 사용자 환경 설정 #1 ]
    //====================================================================================================
    // Html 페이지의 인코딩이 utf-8 로 구성되어 있으면 "ibText.setPageEncoding("utf-8")" 로 설정하십시오.
    // 한글 헤더가 있는 그리드에서 엑셀 로딩이 동작하지 않으면 이 값을 바꿔 보십시오.
    // Down2Excel.jsp 에서의 설정값과 동일하게 바꿔주십시오.
    //====================================================================================================
	ibText.setPageEncoding("UTF-8");

    //ibText.setLoadEncoding("EUC-KR");
    //====================================================================================================
    // [ 사용자 환경 설정 #2 ]
	//====================================================================================================
	// LoadText 용도의 파일을 업로드하여 임시보관할 임시폴더경로를 지정해 주십시오.
	// 예 : "C:/tmp/"   "/usr/tmp/"
	//====================================================================================================
	ibText.setTempFolder("c:/Temp/");

	//====================================================================================================
	// [ 사용자 환경 설정 #3 ]
	//====================================================================================================
	// 시트에 포함될 문자열 중 STX(\u0002), ETX(\u0003) 이 포함된 경우에만 설정해주세요.
	// 설정을 원하지 않는 경우 주석처리해주세요.
	// 0 : 시트 구분자로 STX, ETX 문자를 사용합니다. (기본값)
	// 1 : 시트 구분자로 변형된 문자열을 사용합니다. (시트에 설정이 되어 있어야 합니다.)
	//====================================================================================================
	// ibText.setDelimMode(0);

	//====================================================================================================
	// [ 사용자 환경 설정 #4 ]
	//====================================================================================================
	// 텍스트 파일을 업로드할때 맨앞에 추가되는 BOM(Byte Order Mark) 을 제거하는 경우 설정해주세요.
	//====================================================================================================
	ibText.setRemoveBom(true);

	try {
		out.print(ibText.LoadText());
	} catch(Exception e) {
		out.println("<script>try{var targetWnd = null; if(opener!=null) {targetWnd = opener;} else {targetWnd = parent;} targetWnd.Grids[targetWnd.Grids.g_targetTextSheetID].ShowAlert('텍스트 파일을 읽는 도중 예외가 발생하였습니다.', 'U'); targetWnd.Grids.OnLoadExcelError(targetWnd.Grids[targetWnd.Grids.g_targetTextSheetID], 'TEXT'); }catch(e){}</script>");

		e.printStackTrace();
	} catch (Error e) {
		out.println("<script>try{var targetWnd = null; if(opener!=null) {targetWnd = opener;} else {targetWnd = parent;} targetWnd.Grids[targetWnd.Grids.g_targetTextSheetID].ShowAlert('텍스트 파일을 읽는 도중 예외가 발생하였습니다.', 'U'); targetWnd.Grids.OnLoadExcelError(targetWnd.Grids[targetWnd.Grids.g_targetTextSheetID], 'TEXT'); }catch(e){}</script>");

		e.printStackTrace();
	}
%>
