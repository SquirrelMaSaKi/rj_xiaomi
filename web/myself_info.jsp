<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
 <meta name="viewport" content="width=device-width, initial-scale=1">
 
 
<title>个人中心-我的个人中心</title>
<link rel="stylesheet" type="text/css" href="css/login2.css">
<link rel="stylesheet" href="css/bootstrap.min.css" />
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

</head>
<body>
<%@ include file="header.jsp"%>
<!--网站中间内容开始-->
<div id="dingdanxiangqing_body">
    <div id="dingdanxiangqing_body_big">
        <div id="big_left">
           	   <p style="font-size:18px;margin-top: 15px">订单中心</p>
               <a id="big_left_a" href="${pageContext.request.contextPath }/orderservlet?method=getOrderList">我的订单</a><br/>
               <a id="big_left_a" href="">意外保</a><br/>
               <a id="big_left_a" href="">团购订单</a><br/>
               <a id="big_left_a" href="">评价晒单</a><br/>
               <p style="font-size:18px;margin-top: 15px">个人中心</p>
               <a id="big_left_a" href="self_info.html">我的个人中心</a><br/>
               <a id="big_left_a" href="">消息通知</a><br/>
               <a id="big_left_a" href="">优惠券</a><br/>
               <a id="big_left_a" href="${pageContext.request.contextPath}/userservlet?method=getAddress">收货地址</a><br/>
        </div>
     <div id="big_right" style="height: 500px;overflow: scroll;">
	 	 <h3 style="margin-bottom: 20px; margin-left: 20px">我的个人中心</h3>
		 <div style="margin:0 20px;">
	         <h4>用户名:${user.username}</h4>
	         <h4>邮箱:${user.email}</h4>
	         <h4>性别:${user.gender}</h4>
	         <h4>用户状态:${user.flag==1 ? "正常" : user.flag == 0?"未激活": "失效"}</h4>
	         <h4>级别:普通用户</h4>
			 <a class="btn btn-default btn-sm" data-toggle="modal" data-target="#myModal1">修改密码</a>
			 <div class="text-left" style="color: red">${msg}</div>
		 </div>

		 <div class="modal fade" tabindex="-1" role="dialog" id="myModal1">
			 <div class="modal-dialog" role="document">
				 <div class="modal-content">
					 <div class="modal-header">
						 <button type="button" class="close" data-dismiss="modal">
							 <span>&times;</span>
						 </button>
						 <h4 class="modal-title">修改密码</h4>
					 </div>
					 <form action="userservlet?method=updatePassword" method="post" class="form-horizontal">
						 <div class="motal-body">
							 <div class="form-group">
								 <label class="col-sm-2 control-label">原始密码</label>
								 <div class="col-sm-10">
									 <input type="password" name="password" class="form-control" value="">
								 </div>
							 </div>
							 <div class="form-group">
								 <label class="col-sm-2 control-label">新密码</label>
								 <div class="col-sm-10">
									 <input type="password" name="newpassword" class="form-control" value="">
								 </div>
							 </div>
							 <div class="form-group">
								 <label class="col-sm-2 control-label">确认密码</label>
								 <div class="col-sm-10">
									 <input type="password" name="newpassword2" class="form-control" value="">
								 </div>
							 </div>

						 </div>
						 <div class="motal-footer text-center" style="margin-bottom: 20px">
							 <button type="submit" class="btn btn-primary">修改</button>
						 </div>
					 </form>
				 </div>
			 </div>
		 </div>
	 </div>
    </div>
</div>
	
<!-- 底部 -->
<%@ include file="footer.jsp"%>

</body>
</html>