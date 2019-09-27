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

<title>商品分类</title>
</head>
<body>
<div class="row" style="width:98%;margin-left: 1%;">
	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				商品类型
			</div>
			<div style="color: red;margin-right: 20px" class="pull-right">${msg}</div>
			<div class="panel-body">
				<div class="row">
					<form action="goodstypeservlet?method=showGoodsType" method="post">
						<div class="col-xs-5 col-sm-5 col-md-5 col-lg-5">
							<div class="form-group form-inline">
								<span>类型名称</span>
								<input type="text" name="name" class="form-control">
							</div>
						</div>
						<div class="col-xs-5 col-sm-5 col-md-5 col-lg-5">
							<div class="form-group form-inline">
								<span>类型等级</span>
								<input type="text" name="level" class="form-control">
							</div>
						</div>
						<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
							<!--搜索-->
							<button type="submit" class="btn btn-primary" id="search"><span class="glyphicon glyphicon-search"></span></button>
						</div>
					</form>
				</div>
				<div style="height: 320px;overflow: scroll;">
				<table id="tb_list" class="table table-striped table-hover table-bordered">
					<tr>
						<td>序号</td><td>类型</td><td>等级</td><td>所属类型</td><td>操作</td>
					</tr>
					<c:forEach items="${pageBean.data}" var="gtype" varStatus="i">
					<tr>
						<td>${i.count}</td>
						<td>${gtype.name}</td>
						<td>${gtype.level}</td>
						<td>${gtype.parentName}</td>
						<td>
							<%--<button>修改</button>&nbsp;&nbsp;--%>
							<button class="btn btn-default btn-sm" data-toggle="modal" data-target="#myModal${gtype.id}">修改</button>&nbsp;&nbsp;
							<button onclick="return confirm('确定删除吗？')"> <a href="goodstypeservlet?method=deleteGoodsType&id=${gtype.id}" >删除</a> </button>
						</td>
					</tr>

						<!--弹出模态框-->
						<div class="modal fade" tabindex="-1" role="dialog" id="myModal${gtype.id}">
							<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal">
											<span>&times;</span>
										</button>
										<h4 class="modal-title">修改品类</h4>
									</div>
									<div style="height: 200px">
										<form action="goodstypeservlet?method=modifyGoodsType" method="post" class="form-horizontal">
											<div class="motal-body">
												<div class="form-group" >
													<label class="col-sm-2 control-label">类型名称</label>
													<div class="col-sm-10">
														<input type="hidden" name="id" value="${gtype.id}">
														<input type="text" name="name" class="form-control" value="${gtype.name}">
													</div>
												</div>
												<div class="form-group" >
													<label class="col-sm-2 control-label">品类级别</label>
													<div class="col-sm-10">
														<input type="text" name="level" class="form-control" value="${gtype.level}">
													</div>
												</div>
												<div class="form-group" >
													<label class="col-sm-2 control-label">所属类型</label>
													<div class="col-sm-10">
														<select name="parent">
															<c:forEach var="e" items="${types}">
																<option value="${e.id}" <c:if test="${gtype.parent eq e.id}"> selected="selected"  </c:if>>${e.name}</option>
															</c:forEach>
														</select>
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
					</c:forEach>
				</table>
				</div>
			</div>
			<nav aria-label="..." class="text-center">
				<ul id="mypage" class="pagination">

				</ul>
			</nav>
		</div>
	</div>
</div>
<script type="text/javascript">
    $(function () {
        /*//条件查询
        //给查询按钮 添加 点击事件
        $("#search").click(function(){
			var name = $("input[name='name']").val();
            var level = $("input[name='level']").val();
            /!*var gender = "";
            for(var i=0;i<genders.length;i++){
                if(genders[i].checked){
                    gender += genders[i].value;
                }
            }*!/
            //使用ajax 进行异步交互
            $.ajax({
                url:"<%--${pageContext.request.contextPath}--%>/admin/goodstypeservlet?method=showGoodsType&name="+name+"&level="+level,
                method:"get",
                success:function(data){
                    if(data==0){
                        alert("未找到指定内容");
                        $("input[name='name']").val("");
                        $("input[name='level']").val("");
                    }
                },
                error:function(XMLHttpRequest,textStatus,errorThrown){
                    alert("失败"+XMLHttpRequest.status+":"+textStatus+":"+errorThrown);
                }
            })
        })*/


		//分页导航
        //清空
        $("#mypage").empty();

        //添加上一页
        $("#mypage").append("<li><a href='goodstypeservlet?method=showGoodsType&pageNum=${pageBean.pageNum-1}&pageSize=${pageBean.pageSize}&name=${name}&level=${level}'>«</a></li>");


        //显示导航
        for (var i=${pageBean.startPage};i<=${pageBean.endPage};i++) {
            if (${pageBean.pageNum} == i) {
                $("#mypage").append("<li class='active'><a href='goodstypeservlet?method=showGoodsType&pageNum="+i+"&pageSize=${pageBean.pageSize}&name=${name}&level=${level}'>" + i + "</a></li>");

            } else {
                $("#mypage").append("<li><a href='goodstypeservlet?method=showGoodsType&pageNum="+i+"&pageSize=${pageBean.pageSize}&name=${name}&level=${level}'>" + i + "</a></li>");
            }
        }

        //添加下一页
        $("#mypage").append("<li><a href='goodstypeservlet?method=showGoodsType&pageNum=${pageBean.pageNum+1}&pageSize=${pageBean.pageSize}&name=${name}&levle=${level}'>»</a></li>");
    });

</script>
</body>
</html>