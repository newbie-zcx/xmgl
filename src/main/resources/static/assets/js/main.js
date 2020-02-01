// 加载admin、index模块
layui.use(['index'], function () {
    var $ = layui.jquery;
    var index = layui.index;

    // 默认加载主页
    index.loadHome({
        menuPath: 'https://i.snssdk.com/ugc/hotboard_fe/hot_list/template/hot_list/forum_tab.html',
        menuName: '<i class="layui-icon layui-icon-home"></i>'
    });

    // 默认加载第一个菜单
  /*  index.loadView({
        menuPath: $('.layui-side .layui-nav .layui-nav-item a[lay-href!="javascript:;"]:first').attr('lay-href'),
        menuName: '<i class="layui-icon layui-icon-home"></i>'
    });*/

});