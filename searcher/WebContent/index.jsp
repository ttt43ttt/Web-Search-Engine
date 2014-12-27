<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.wilson.common.*"%>
<%@page import="com.wilson.search.SearchResult"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SJTU BBS Search Engine</title>
<script type="text/javascript" src="jquery-2.1.3.min.js"></script>
<script type="text/javascript">
	$(function() {
		$('#search').on('click', function() {
			var kw = $('#kw').val();
			window.location.href = 's?kw=' + kw;
		});
	});
</script>
</head>
<body>
	<div>
		<input id="kw" type="text"
			value="<%String kw = (String) request.getAttribute("kw");
			if (kw != null) {
				out.print(kw);
			}%>" /> <input
			id="search" type="button" value="搜索" />
	</div>
	<div>
		<%
			List<SearchResult> result = (List<SearchResult>) request.getAttribute("result");
			if (result != null) {
				for (SearchResult record : result) {
					IndexInfo indexInfo = record.getIndexInfo();
					PageInfo pageInfo = record.getPageInfo();
		%>
		<div>
			<div>
				<a target="_blank" href="<%=pageInfo.getUrl()%>"><%=pageInfo.getTitle()%></a>
			</div>
			<div>
				<%
					String content = pageInfo.getContent();

							if (content.length() < 100) {
								out.print(content);
							} else {
								StringBuilder sb = new StringBuilder();

								for (int position : indexInfo.getPositions()) {
									int begin = Math.max(0, position - 5);
									int end = Math.min(position + 10, content.length() - 1);
									String text = content.substring(begin, end);
									sb.append(text);
									sb.append("...");

									if (sb.length() > 100) {
										break;
									}
								}

								out.print(sb.toString());
							}
				%>
			</div>
		</div>
		<%
			}
			}
		%>
	</div>
</body>
</html>