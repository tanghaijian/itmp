/*******
 * niexingquan
 * 2019-10-28
 * 项目管理-交付视图js
 * ********/
$(() => {
    downOrUpButton();
    buttonClear();
    chartInit();
});

function chartInit() {
    //需求状态分布统计图    
    var myChart = echarts.init(document.getElementById('main'));
    //需求状态 饼图
    var option = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        grid: { containLabel: true },
        series: [
            {
                name: '需求状态',
                type: 'pie',
                selectedMode: 'single',
                radius: [0, '50%'],
                label: {
                    formatter: '{b}\n{d}%'
                },
                data: [
                    { value: 38, name: '待实施', selected: true },
                    { value: 28, name: '实施中' },
                    { value: 20, name: '实施完成' },
                    { value: 10, name: '取消' },
                    { value: 6, name: '已上线' },
                ]
            },
        ]
    };
    myChart.setOption(option);

    //开发任务状态分布统计图
    var myChart2 = echarts.init(document.getElementById('main2'));
    //开发任务 饼图
    var option2 = {
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        grid: { containLabel: true },

        series: [
            {
                name: '开发任务',
                type: 'pie',
                selectedMode: 'single',
                radius: [0, '50%'],
                label: {
                    formatter: '{b}\n{d}%'
                },
                data: [
                    { value: 38, name: '需求处理中', selected: true },
                    { value: 28, name: '需求实施完成' },
                    { value: 20, name: '需求分析审核' },
                    { value: 10, name: '需求IT设计中' },
                    { value: 6, name: '已上线' },
                ]
            },
        ]
    };
    myChart2.setOption(option2);

    // 需求交付进度统计     
    var myChart3 = echarts.init(document.getElementById('main3'));
    // 显示标题，图例和空的坐标轴
    myChart3.setOption({
        title: {
            subtext: '提示：点击需求进度弹出需求交付明细窗口',
            x: 'right',
        },
        grid: { containLabel: true },
        xAxis: { name: '进度' },
        yAxis: { name: '需求', type: 'category' },
    });

    //系统交付进度统计
    var myChart4 = echarts.init(document.getElementById('main4'));
    myChart4.setOption({
        grid: { containLabel: true },
        xAxis: { name: '进度' },
        yAxis: { name: '系统', type: 'category' },
    });
    setTimeout(() => {
        chart_3();
        chart_4();
    }, 1000)

    // 需求交付进度统计  柱状图
    function chart_3() {
        var _data = [
            [89.3, 58212, '线上事业部', '20190903103929R', 1],
            [57.1, 78254, '2线上事业部', '20190903103929R', 2],
        ]
        //   var _source =  [
        //       ['score', 'amount', 'product','num','id'],
        //   ]
        //   _data.map(function(val,idx){
        //       _source.push(val)
        //   })
        var option3 = {
            dataset: {
                source: [
                    ['score', 'amount', 'product', 'num', 'id'],
                    [57.1, 58212, '线上事业部', '20190903103929R', 1],
                    [89.3, 78254, '线上产品部', '20190903232232R', 2],
                ]
            },
            yAxis: {
                data: ['线上事业部', '线上产品部'],
                axisLabel: {
                    formatter: function (value, idx) {
                        return value + '\n' + _data[idx][3];
                    },
                }
            },
            tooltip: {
                trigger: 'axis',
                data: ['线上事业部', '线上产品部'],
                axisPointer: {
                    type: 'shadow',  //hover阴影
                },
            },
            series: [
                {
                    type: 'bar',
                    barMinHeight: 30, //最小柱高
                    barMaxWidth:50, // 最大柱宽度
                    z: 10, // 控制图表前后顺序
                    itemStyle: { // 柱子样式
                        normal: {
                            // color: '#ff6600', // 柱状图颜色
                            label: {
                                show: true, // 显示文本
                                position: 'top', // 数据值位置
                                formatter: function(params){
                                    return params.data[0]+'%'
                                },
                                textStyle: {
                                    color: '#000'
                                }
                            }
                        }
                    },
                    encode: {
                        x: '',
                        y: 'product',
                    }
                }
            ],
            // dataZoom: [{ // 这个dataZoom组件，默认控制x轴。
            //     type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
            //     start: 0, // 左边开始位置。
            //     end: 100 // 右边的位置。
            // }],
        };
        myChart3.setOption(option3);
    }

    // 系统交付进度统计  柱状图
    function chart_4() {
        var option4 = {
            dataset: {
                source: [
                    ['score', 'amount', 'product'],
                    [89.3, 78254, '收付费系统'],
                    [57.1, 58212, '意健险理赔系统'],
                ]
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'  //hover阴影
                }
            },
            series: [
                {
                    type: 'bar',
                    barMinHeight: 30, //最小柱高
                    barMaxWidth:50, // 最大柱宽度
                    z: 10, // 控制图表前后顺序
                    itemStyle: { // 柱子样式
                        normal: {
                            color: '#ff6600', // 柱状图颜色
                            label: {
                                show: true, // 显示文本
                                position: 'top', // 数据值位置
                                formatter: function(params){
                                    return params.data[0]+'%'
                                },
                                textStyle: {
                                    color: '#000'
                                }
                            }
                        }
                    },
                    encode: {
                        x: '',
                        y: 'product'
                    }
                }
            ],
        };
        myChart4.setOption(option4);
    }

    var myChart5 = echarts.init(document.getElementById('main5'));
    var myChart6 = echarts.init(document.getElementById('main6'));
    var myChart9 = echarts.init(document.getElementById('main9'));
    $('#requirementMapModal').on('hide.bs.modal', function () {
        myChart9.clear();
        myChart5.clear();
    })
    $('#systemMapModal').on('hide.bs.modal', function () {
        myChart6.clear();
    })
    myChart3.on('click', function (params) {
        $('#requirementMapModal').modal('show');
        chart_9(params.data[2] + params.data[3]);
        chart_5('收付费系统');
    });

    //交付明细  饼图
    function chart_5(params) {
        var _data = [
            { value: 38, name: '已上线', selected: true },
            { value: 28, name: '实施中' },
            { value: 20, name: '实施完成' },
            { value: 10, name: '取消' },
            { value: 6, name: '待实施' },
        ]
        //需求交付明细    
        var option5 = {
            title: {
                text: params + '-开发任务状态分布统计图',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            grid: { containLabel: true },
            series: [
                {
                    name: '开发任务',
                    type: 'pie',
                    selectedMode: 'single',
                    radius: [0, '50%'],
                    label: {
                        formatter: '{b}\n{d}%',
                    },
                    data: _data
                },
            ]
        };
        myChart5.setOption(option5);
    }

    //交付明细 柱状图
    function chart_9(params) {
        var source_data = [
            [57.1, 58212, '意健险理赔系统', 1],
            [89.3, 78254, '收付费系统', 2],
        ]
        var _source_ = [
            ['score', 'amount', 'product'],
        ]
        source_data.map(function (val, idx) {
            _source_.push(val)
        })
        var option9 = {
            title: {
                text: params + '-交付明细柱状图',
                x: 'center'
            },
            grid: { containLabel: true },
            xAxis: { name: '交付进度' },
            yAxis: { name: '系统', type: 'category' },
            dataset: {
                source: _source_
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'  //hover阴影
                }
            },
            series: [
                {
                    type: 'bar',
                    barMaxWidth:50, // 最大柱宽度
                    itemStyle: { // 柱子样式
                        normal: {
                            color: '#ff6600', // 柱状图颜色
                            label: {
                                show: true, // 显示文本
                                position: 'top', // 数据值位置
                                formatter: function(params){
                                    return params.data[0]+'%'
                                },
                            }
                        }
                    },
                    label: {
                        normal: {
                            show: true,
                            position: 'inside',
                        }
                    },
                    encode: {
                        x: '',
                        y: 'product'
                    }
                }
            ]
        };
        myChart9.setOption(option9);
    }

    myChart9.on('click', function (params) {
        var _data = []
        if (params.data[3] == 1) {
            _data = [
                { value: 38, name: '已上线', selected: true },
                { value: 28, name: '实施中' },
                { value: 20, name: '实施完成' },
                { value: 10, name: '取消' },
                { value: 6, name: '待实施' },
            ]
        } else {
            _data = [
                { value: 18, name: '已上线', selected: true },
                { value: 2, name: '实施中' },
                { value: 10, name: '实施完成' },
                { value: 30, name: '取消' },
                { value: 22, name: '待实施' },
            ]
        }
        //需求交付明细    
        myChart5.clear();
        chart_5(params.data[2]);
    });

    myChart4.on('click', function (params) {
        var _data = []
        if (params.data[3] == 1) {
            _data = [
                { value: 38, name: '已上线', selected: true },
                { value: 28, name: '实施中' },
                { value: 20, name: '实施完成' },
                { value: 10, name: '取消' },
                { value: 6, name: '待实施' },
            ]
        } else {
            _data = [
                { value: 18, name: '已上线', selected: true },
                { value: 2, name: '实施中' },
                { value: 10, name: '实施完成' },
                { value: 30, name: '取消' },
                { value: 22, name: '待实施' },
            ]
        }
        //系统交付明细    
        var option6 = {
            title: {
                text: params.data[2] + '-开发任务状态分布统计图',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            grid: { containLabel: true },
            series: [
                {
                    name: '开发任务',
                    type: 'pie',
                    selectedMode: 'single',
                    radius: [0, '50%'],
                    label: {
                        formatter: '{b}\n{d}%',
                    },
                    data: _data
                },
            ]
        };
        $('#systemMapModal').modal('show');
        myChart6.setOption(option6);
    });
}