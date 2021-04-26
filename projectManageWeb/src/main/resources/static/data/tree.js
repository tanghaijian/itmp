var data = {
  overview:{ //项目概览
    projectName:'IT开发测试管理系统项目',
    projectCode:'PRJ-20190001',
    toState:'进行中',//进行状态
    projectStatus:'延期',//项目状态
    devMode:'传统',//开发模式
    interactedSystem:'IT综合管理平台项目',//关联系统
    projectType:'应用系统类',
    projectWeek:'2019-8-16 ~ 2019-8-16',
    budgetCode:'YS-20190001',
    projectManager:'王小军',
    rimSystem:[{id:1,systemName:'IT全流程平台'},{id:5,systemName:' ITSM'},{id:2,systemName:'大地集团统一单点登录系统'},
      {id:3,systemName:'自动化运维管理系统'}],//周边系统
    projectUsers:[{id:1,userName:'王老虎'},{id:10,userName:'麻溜'},{id:2,userName:'张统'}],//项目成员
  },
  work:{
    allWorks:21,//总任务数
    stayWork:3,//待实施
    inForceWork:7,//实施中
    completeWork:6,//已完成
    delayWork:3,//已延期
    cancelWork:2,//已取消
    //下一冲刺
    nextAllWorks:1,//总任务数
    nextStayWork:31,//待实施
    nextInForceWork:17,//实施中
    nextCompleteWork:16,//已完成
    nextDelayWork:31,//已延期
    nextCancelWork:12,//已取消
    //变更
    updateAddWork:3,//本周新增
    updateAllWork:12,//变更总数
    updateAffirmWork:3,//待确认数
    //风险
    riskeAddWork:3,//本周新增
    riskAllWork:12,//风险总数
    riskUnsolvedWork:3,//未解决数
    milestones:[{
      name:'里程碑名称里程碑名称（王小军）',
      date:'2019-8-16 ~ 2019-8-16',
      progress:'100',
    },{
      name:'里程碑名称里程碑名称（王小军）',
      date:'2019-8-16 ~ 2019-8-16',
      progress:'80',
    },{
      name:'里程碑名称里程碑名称（王小军）',
      date:'2019-8-16 ~ 2019-8-16',
      progress:'20',
    },{
      name:'里程碑名称里程碑名称（王小军）',
      date:'2019-8-16 ~ 2019-8-16',
      progress:'0',
    },]
  },
  dynamic:[{
    title:'里程碑名称里程碑名称（王小军）',
    date:'2018-11-20 11:26:53',
    text:'消息内容正文，消息内容正文消息内容正文，消息内容正文消息内容正文，消息内容正文消息内容正文，消息内容正文消息…',
  },{
    title:'里程碑名称里程碑名称（王小军）',
    date:'2018-11-20 11:26:53',
    text:'消息内容正文，消息内容正文消息内容正文，消息内容正文消息内容正文，消息内容正文消息内容正文，消息内容正文消息…',
  },{
    title:'里程碑名称里程碑名称（王小军）',
    date:'2018-11-20 11:26:53',
    text:'消息内容正文，消息内容正文消息内容正文，消息内容正文消息内容正文，消息内容正文消息内容正文，消息内容正文消息…',
  },{
    title:'里程碑名称里程碑名称（王小军）',
    date:'2018-11-20 11:26:53',
    text:'消息内容正文，消息内容正文消息内容正文，消息内容正文消息内容正文，消息内容正文消息内容正文，消息内容正文消息…',
  },],
  document:[
    { id: 1, pId: 0, fileName: "父节点1 - 展开", open: true },
    { id: 11, pId: 1, fileName: "父节点11 - 折叠" },
    { id: 111, pId: 11, fileName: "叶子节点111" },
    { id: 112, pId: 11, fileName: "叶子节点112" },
    { id: 113, pId: 11, fileName: "叶子节点113" },
    { id: 114, pId: 11, fileName: "叶子节点114" },
    { id: 12, pId: 1, fileName: "父节点12 - 折叠" },
    { id: 121, pId: 12, fileName: "叶子节点121" },
    { id: 122, pId: 12, fileName: "叶子节点122" },
    { id: 123, pId: 12, fileName: "叶子节点123" },
    { id: 124, pId: 12, fileName: "叶子节点124" },
    { id: 13, pId: 1, fileName: "父节点13 - 没有子节点", isParent: true },
    { id: 2, pId: 0, fileName: "父节点2 - 折叠" },
    { id: 21, pId: 2, fileName: "父节点21 - 展开", open: true },
    { id: 211, pId: 21, fileName: "叶子节点211" },
    { id: 212, pId: 21, fileName: "叶子节点212" },
    { id: 213, pId: 21, fileName: "叶子节点213" },
    { id: 214, pId: 21, fileName: "叶子节点214" },
    { id: 22, pId: 2, fileName: "父节点22 - 折叠" },
    { id: 221, pId: 22, fileName: "叶子节点221" },
    { id: 222, pId: 22, fileName: "叶子节点222" },
    { id: 223, pId: 22, fileName: "叶子节点223" },
    { id: 224, pId: 22, fileName: "叶子节点224" },
    { id: 23, pId: 2, fileName: "父节点23 - 折叠" },
    { id: 231, pId: 23, fileName: "叶子节点231" },
    { id: 232, pId: 23, fileName: "叶子节点232" },
    { id: 233, pId: 23, fileName: "叶子节点233" },
    { id: 234, pId: 23, fileName: "叶子节点234" },
    { id: 3, pId: 0, fileName: "父节点3 - 没有子节点", isParent: true }
  ]
}