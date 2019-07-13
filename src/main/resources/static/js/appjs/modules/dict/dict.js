var prefix = "/modules/dict";
$(function() {
	load();
});

function selectLoad() {
	var html = "";
	$.ajax({
		url : '/modules/dict/type',
		success : function(data) {
			//加载数据
			for (var i = 0; i < data.length; i++) {
				html += '<option value="' + data[i].type + '">' + data[i].description + '</option>'
			}
			$(".chosen-select").append(html);
			$(".chosen-select").chosen({
				maxHeight : 200
			});
			//点击事件
			$('.chosen-select').on('change', function(e, params) {
				console.log(params.selected);
				var opt = {
					query : {
						type : params.selected
					}
				};
				$('#exampleTable').bootstrapTable('refresh', opt);
			});
		}
	});
}

function load() {
	selectLoad();
	$('#exampleTable')
		// .bootstrapTable(
		// 	{
		// 		method : 'get', // 服务器数据的请求方式 get or post
		// 		url : prefix + "/list", // 服务器数据的加载地址
		// 		//	showRefresh : true,
		// 		//	showToggle : true,
		// 		//	showColumns : true,
		// 		iconSize : 'outline',
		// 		toolbar : '#exampleToolbar',
		// 		striped : true, // 设置为true会有隔行变色效果
		// 		dataType : "json", // 服务器返回的数据类型
		// 		pagination : true, // 设置为true会在底部显示分页条
		// 		// queryParamsType : "limit",
		// 		// //设置为limit则会发送符合RESTFull格式的参数
		// 		singleSelect : false, // 设置为true将禁止多选
		// 		// contentType : "application/x-www-form-urlencoded",
		// 		// //发送到服务器的数据编码类型
		// 		pageSize : 10, // 如果设置了分页，每页数据条数
		// 		pageNumber : 1, // 如果设置了分布，首页页码
		// 		//search : true, // 是否显示搜索框
		// 		showColumns : false, // 是否显示内容下拉框（选择显示的列）
		// 		sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
		// 		queryParams : function(params) {
		// 			return {
		// 				//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
		// 				limit : params.limit,
		// 				offset : params.offset,
		// 				// name:$('#searchName').val(),
		// 				type : $('#searchName').val()
		// 			};
		// 		},
		// 		// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
		// 		// queryParamsType = 'limit' ,返回参数必须包含
		// 		// limit, offset, search, sort, order 否则, 需要包含:
		// 		// pageSize, pageNumber, searchText, sortName,
		// 		// sortOrder.
		// 		// 返回false将会终止请求
		// 		columns : [
		.bootstrapTreeTable(
			{
				id: 'id',
				code: 'id',
				parentCode: 'parentId',
				type: "GET", // 请求数据的ajax类型
				url: prefix + '/treelist', // 请求数据的ajax的url
				// ajaxParams: {sort:'order_num'}, // 请求数据的ajax的data属性
				expandColumn: '1',// 在哪一列上面显示展开按钮
				striped: true, // 是否各行渐变色
				bordered: true, // 是否显示边框
				expandAll: false, // 是否全部展开
				// toolbar : '#exampleToolbar',
				columns: [

					// {
					// 	checkbox : true
					// },
					{
						field : 'id',
						title : '编号'
					},
					{
						field : 'name',
						title : '标签名'
					},
					{
						field : 'value',
						title : '数据值',
						width : '100px'
					},
					{
						field : 'type',
						title : '类型'
					},
					{
						field : 'description',
						title : '描述'
					},
					// {
					// 	visible : false,
					// 	field : 'sort',
					// 	title : '排序（升序）'
					// },
					{
						visible : false,
						field : 'parentId',
						title : '父级编号'
					},
					// {
					// 	visible : false,
					// 	field : 'createBy',
					// 	title : '创建者'
					// },
					// {
					// 	visible : false,
					// 	field : 'createDate',
					// 	title : '创建时间'
					// },
					// {
					// 	visible : false,
					// 	field : 'updateBy',
					// 	title : '更新者'
					// },
					// {
					// 	visible : false,
					// 	field : 'updateDate',
					// 	title : '更新时间'
					// },
					{
						field : 'remarks',
						title : '备注信息'
					},
					// {
					// 	visible : false,
					// 	field : 'delFlag',
					// 	title : '删除标记'
					// },
					{
						title : '操作',
						field : 'id',
						align : 'center',
						formatter : function(item, index) {
							var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
								+ item.id
								+ '\')"><i class="fa fa-edit"></i></a> ';
							var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
								+ item.id
								+ '\')"><i class="fa fa-remove"></i></a> ';
							if (item.parentId == "0") {
								var f = '<a class="btn btn-success btn-sm ' + s_add_h + '" href="#" title="增加下级"  mce_href="#" onclick="add(\''
									+ item.id + '\')"><i class="fa fa-plus"></i></a> ';
								return e + d +f;
							}else{
								var f = '<a class="btn btn-success btn-sm ' + s_add_h + '" href="#" title="增加"  mce_href="#" onclick="addD(\''
									+item.id +'\',\''+ item.type +'\',\''+item.description
									+'\')"><i class="fa fa-plus"></i></a> ';
								return e + d +f;
							}
							return e + d;
						}
					} ]
			});
}

function reLoad() {
	// var opt = {
	// 	query : {
	// 		type : $('.chosen-select').val()
	// 	}
	// };
	// $('#exampleTable').bootstrapTable('refresh');
	load();
}

function add(id) {
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add/' +id// iframe的url
	});
}

function edit(id) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
}

function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix + "/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.state === 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})
}

function addD(id,type,description) {
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add/'+id+'/'+type+'/'+description+'/'// iframe的url
	});
}

function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length === 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['id'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.state === 0) {
					alert("2");
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {});
}