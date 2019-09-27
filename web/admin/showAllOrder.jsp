<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>后台 订单列表</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="../css/bootstrap.min.css">
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script type="text/javascript">
	function sendOrder(id){
	    var b = confirm("确定要修改吗");
	    if (b) {
            location.href = "${pageContext.request.contextPath}/admin/orderservlet?method=sendOrder&oid="+id;
        }
	}
	$(function(){
		$("#search").click(function(){
			var username = $("input[name='username']").val();
			var status = $("select[name='orderStatus'] option:selected").val();
			location.href="${pageContext.request.contextPath}/admin/orderservlet?method=getAllOrder&username="+username+"&orderStatus="+status;
		})
	})
</script>
</head>
<body>
<div class="row" style="width:98%;margin-left: 1%;margin-top: 5px;">
	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				订单列表
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-xs-5 col-sm-5 col-md-5 col-lg-5">
						<div class="form-group form-inline">
							<span>用户姓名</span>
							<input type="text" name="username" class="form-control" value="${username}">
						</div>
					</div>
					<div class="col-xs-5 col-sm-5 col-md-5 col-lg-5">
						<div class="form-group form-inline">
							<span>订单状态</span>
							<select name="orderStatus" class="form-control">
								<option value=" ">----------</option>
								<option value="1" ${status=='1'?'selected':''}>未支付</option>
								<option value="2" ${status=='2'?'selected':''}>已支付,待发货</option>
								<option value="3" ${status=='3'?'selected':''}>已发货,待收货</option>
								<option value="4" ${status=='4'?'selected':''}>完成订单</option>
							</select>
						</div>
					</div>
					<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
						<button type="button" class="btn btn-primary" id="search"><span class="glyphicon glyphicon-search"></span></button>
					</div>
				</div>
				
				<table id="tb_list" class="table table-striped table-hover table-bordered table-condensed">
					<tr>
						<td>序号</td>
						<td>订单编号</td>
						<td>总金额</td>
						<td>订单状态</td>
						<td>订单时间</td>
						<td>用户姓名</td>
						<td>操作</td>
					</tr>
					<c:forEach items="${pageBean.data}" var="order" varStatus="i">
					<tr>
						<td>${i.count}</td>
						<td>${order.id}</td>
						<td>${order.money}</td>
						<td>
							<c:if test="${order.status eq 1}">
								未支付
							</c:if>
							<c:if test="${order.status eq 2}">
								已支付,待发货
							</c:if>
							<c:if test="${order.status eq 3}">
								已发货,待收货
							</c:if>
							<c:if test="${order.status eq 4}">
								订单完成
							</c:if>
						</td>
						<td>${order.time}</td>
						<td>${order.username}</td>
						<td>
							<c:if test="${order.status eq 2}">
								<button type="button" class="btn btn-danger btn-sm" onclick="sendOrder('${order.id}')">发货</button>
							</c:if>
						</td>
					</tr>
					</c:forEach>
					
				</table>
			</div>
			<nav aria-label="..." class="text-center">
				<ul id="mypage" class="pagination">

				</ul>
			</nav>
		</div>
	</div>
</div>
</body>
<
<script type="text/javascript">
	$(function () {
        //清空
        $("#mypage").empty();

        //添加上一页
        $("#mypage").append("<li><a href='orderservlet?method=getAllOrder&pageNum=${pageBean.pageNum-1}&pageSize=${pageBean.pageSize}&username=${username}&orderStatus=${status}'>«</a></li>");


        //显示导航
        for (var i=${pageBean.startPage};i<=${pageBean.endPage};i++) {
            if (${pageBean.pageNum} == i) {
                $("#mypage").append("<li class='active'><a href='orderservlet?method=getAllOrder&pageNum="+i+"&pageSize=${pageBean.pageSize}&username=${username}&orderStatus=${status}'>" + i + "</a></li>");

            } else {
                $("#mypage").append("<li><a href='orderservlet?method=getAllOrder&pageNum="+i+"&pageSize=${pageBean.pageSize}&username=${username}&orderStatus=${status}'>" + i + "</a></li>");
            }
        }

        //添加下一页
        $("#mypage").append("<li><a href='orderservlet?method=getAllOrder&pageNum=${pageBean.pageNum+1}&pageSize=${pageBean.pageSize}&username=${username}&orderStatus=${status}'>»</a></li>");
    });
</script>
</html>