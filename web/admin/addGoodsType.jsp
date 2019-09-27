<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="../css/bootstrap.min.css" />
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<title>添加商品种类</title>
<script type="text/javascript">
	$(function () {
		$.ajax({
			url:"${pageContext.request.contextPath}/admin/goodstypeservlet?method=showAllGoodsType",
			type:"post",
			dataType:"json",
			contentType:"application/x-www-form-urlencoded",
			success:function (data) {
				for(var i in data) {
				    $("#parent").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
				}
            }
		});
	});
</script>
</head>
<body>
<div style="width:98%;margin-left: 1%;">
	<div class="panel panel-default">
		<div class="panel-heading">
			添加商品种类
		</div>
		<div class="panel-body">
			<form action="${pageContext.request.contextPath }/admin/goodstypeservlet?method=addGoodsType" method="post" enctype="application/x-www-form-urlencoded">
				<div class="row">
					<div class="form-group form-inline">
						<span>所属种类</span>
						<select name="parent" id="parent">
						</select>
					</div>
				</div>
				<div class="row">
					<div class="form-group form-inline">
						<span>种类等级</span>
						<select name="level">
							<option value="0">最高级</option>
							<option value="1">一级</option>
							<option value="2">二级</option>
							<option value="3">三级</option>
							<option value="4">四级</option>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="form-group form-inline">
						<span>种类名称</span>
						<input type="text" name="name" class="form-control">
					</div>
				</div>
				<div class="row">
					<div class="btn-group">
						<button type="reset" class="btn btn-default">清空</button>
						<button type="submit" class="btn btn-default">添加</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>