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
<title>无效会员管理</title>

</head>
<script type="text/javascript">
    $(document).ready(function(){
        loadUser(1,8);
    })
    //连接servlet 获取 数据
    function loadUser(pageNum,pageSize){
        var username = $("input[name='username']").val();
        var genders = $("input[name='gender']");
        var gender = "";
        for(var i=0;i<genders.length;i++){
            if(genders[i].checked){
                gender += genders[i].value;
            }
        }
        if (pageNum < 1) {
            pageNum = 1;
        }
        $.ajax({
            url:"${pageContext.request.contextPath}/admin/userservlet?method=invalidUserList&username="+username+"&gender="+gender+"&pageNum=" + pageNum + "&pageSize=" + pageSize + "&n=" + Math.random(),
            method:"get",
            success:function(data){
                showMsg(data);
            },
            error:function(XMLHttpRequest,textStatus,errorThrown){
                alert("失败"+XMLHttpRequest.status+":"+textStatus+":"+errorThrown);
            }
        });
    }
    //显示用户信息
    function showMsg(data){
        var pageBean = JSON.parse(data);
        var list = pageBean.data;
        $("#tb_list").html("<tr class='tr_head'><td>编号</td><td>邮箱</td><td>姓名</td><td>性别</td><td>类别</td><td>操作</td></tr>");
        var i = 1;
        for(var u in list){
            //声明 tr  td  对象
            var tr = $("<tr></tr>");
            var td1 = $("<td>"+(i++)+"</td>");
            var td2 = $("<td>"+list[u].email+"</td>");
            var td3 = $("<td>"+list[u].username+"</td>");
            var td4 = $("<td>"+list[u].gender+"</td>");
            var td5 = $("<td>"+(list[u].role==0?"管理员":"会员")+"</td>");
            var td6 = $("<td><a href='javascript:delUser("+list[u].id+")' class='btn btn-primary btn-xs'>恢复</a></td>");

            //将td 添加到tr中
            tr.append(td1);
            tr.append(td2);
            tr.append(td3);
            tr.append(td4);
            tr.append(td5);
            tr.append(td6);
            $("#tb_list").append(tr);
        }

        //清空
        $("#mypage").empty();

        //添加上一页
        $("#mypage").append("<li><a href='javascript:loadUser("+(pageBean.pageNum-1)+", "+pageBean.pageSize+")'>«</a></li>");


        //显示导航
        for (var i=pageBean.startPage;i<=pageBean.endPage;i++) {
            if (pageBean.pageNum == i) {
                $("#mypage").append("<li class='active'><a href='javascript:loadUser("+i+", "+pageBean.pageSize+")'>" + i + "</a></li>");

            } else {
                $("#mypage").append("<li><a href='javascript:loadUser("+i+", "+pageBean.pageSize+")'>" + i + "</a></li>");
            }
        }

        //添加下一页
        $("#mypage").append("<li><a href='javascript:loadUser("+(pageBean.pageNum+1)+", "+pageBean.pageSize+")'>»</a></li>");

    }
    //恢复用户
    function delUser(id){
        if(confirm("确认要恢复吗?")){
            $.ajax({
                url:"${pageContext.request.contextPath}/admin/userservlet?method=renewUser&id="+id,
                method:"get",
                success:function(data){
                    loadUser(1,8);
                },
                error:function(XMLHttpRequest,textStatus,errorThrown){
                    alert("失败"+XMLHttpRequest.status+":"+textStatus+":"+errorThrown);
                }
            })
        }
    }
    //条件查询
    $(function(){
        //给查询按钮 添加 点击事件
        $("#search").click(function(){
            var username = $("input[name='username']").val();
            var genders = $("input[name='gender']");
            var gender = "";
            for(var i=0;i<genders.length;i++){
                if(genders[i].checked){
                    gender += genders[i].value;
                }
            }
            //使用ajax 进行异步交互
            $.ajax({
                url:"${pageContext.request.contextPath}/admin/userservlet?method=invalidUserList&username="+username+"&gender="+gender,
                method:"post",
                success:function(data){
                    if(data==0){
                        alert("未找到指定内容");
                        $("input[name='username']").val("");
                        $("input[name='gender']").removeAttr("checked");
                    }else{
                        showMsg(data);
                    }
                },
                error:function(XMLHttpRequest,textStatus,errorThrown){
                    alert("失败"+XMLHttpRequest.status+":"+textStatus+":"+errorThrown);
                }
            })
        })
    })
</script>
<body>
<div class="row" style="width: 100%;">
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading">会员列表</div>
			<div class="panel-body">
				<!-- 条件查询 -->
				<div class="row">
					<div class="col-xs-5 col-sm-5 col-md-5 col-lg-5">
						<div class="form-group form-inline">
							<span>用户姓名</span>
							<input type="text" name="username" class="form-control">
						</div>
					</div>
					<div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
						<div class="form-group form-inline">
							<span>性别</span>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="radio-inline">
								<input type="radio" name="gender" value="男"> 男&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
							<label class="radio-inline">
								<input type="radio"name="gender" value="女"> 女&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
							<label class="radio-inline">
								<input type="radio"name="gender" value=""> 全部
							</label>
						</div>
					</div>
					<div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
						<button type="button" class="btn btn-primary" id="search"><span class="glyphicon glyphicon-search"></span></button>
					</div>
				</div>
				<div class="panel-heading">
					已停用会员列表
				</div>
				<!-- 列表显示 -->
				<table id="tb_list" class="table table-striped table-hover table-bordered">

				</table>
				<div>${msg}</div>
				<nav aria-label="..." class="text-center">
					<ul id="mypage" class="pagination">

					</ul>
				</nav>
			</div>
		</div>
	</div>
</div>
<%--<div class="row" style="width:98%;margin-left: 1%;margin-top: 5px;">
	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				已停用会员列表
			</div>
			<div class="panel-body">
				<table id="tb_list" class="table table-striped table-hover table-bordered">
				</table>
			</div>
		</div>
	</div>
</div>--%>
</body>
</html>