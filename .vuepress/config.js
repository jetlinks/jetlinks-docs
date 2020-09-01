module.exports = {
    title: 'JetLinks 物联网基础平台',
    descirption: '测试文档',
    //basic: './dist',
    port: 9999,
    // configureWebpack : {
    //     output: {
    //         publicPath: "/"
    //     }
    // },
    themeConfig: {
        nav: [
            {text: '关于', link: 'http://jetlinks.cn/'},
            {text: 'GitHub', link: 'https://github.com/jetlinks'},
            {text: 'gitee', link: 'https://gitee.com/jetlinks'},
            {text: '提交问题', link: 'https://github.com/jetlinks/jetlinks-community/issues'},
            {text: '文档纠错', link: 'https://github.com/jetlinks/jetlinks-docs/issues'}
        ],
        sidebar: [
            {
                title: '基础',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/quick-start/introduction.md', '介绍'],
                    //['/quick-start/download.md', '获取源代码'],
                    //['/quick-start/demo.md', '演示'],
                    ['/quick-start/update-log.md', '更新记录']
                ]
            },
            {
                title: '安装部署',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/install-deployment/required-environment.md', '平台所需环境'],
                    ['/install-deployment/docker-start.md', '使用docker快速启动'],
                    ['/install-deployment/ide-docker-start.md', '使用docker+源代码启动开发环境'],
                    ['/install-deployment/ui-start.md', 'UI启动'],
                    ['/install-deployment/not-docker-start.md', '非docker环境启动'],
                    ['/quick-start/demo.md', '快速体验设备接入'],
                    ['/install-deployment/deployment.md', '部署'],
                    ['/install-deployment/cluster.md', '集群部署']
                ]
            },
            {
                title: '进阶',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/advancement-guide/jetlinks-protocol.md', '物模型说明'],
                    ['/basics-guide/protocol-support.md', '设备消息协议解析SDK'],
                    ['/basics-guide/jetlinks-protocol-support.md', 'JetLinks官方协议']
                ]
            },
            {
                title: '最佳实践',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/advancement-guide/mqtt-connection.md', '使用MQTT直接接入设备'],
                    ['/advancement-guide/third-mqtt.md', '通过第三方MQTT服务接入设备'],
                    ['/best-practices/tcp-connection.md', 'TCP透传方式接入设备'],
                    ['/best-practices/http-connection.md', '使用HTTP接入设备(PRO)'],
                    ['/best-practices/coap-connection.md', '使用CoAP接入设备(PRO)'],
                    ['/best-practices/udp-connection.md', '使用UDP接入设备(PRO)'],
                    ['/best-practices/device-gateway-connection.md', '通过网关设备接入多个子设备'],
                    ['/best-practices/auto-register.md', '设备自注册到平台'],
                    ['/best-practices/sort-link.md', 'TCP,MQTT短连接接入'],
                    ['/best-practices/poll-device-data.md', '从第三方或者设备主动拉取数据'],
                    ['/best-practices/device-alarm.md', '设备上报数据,触发设备告警并发送邮件通知.'],
                    ['/best-practices/rule-engine-sql.md', '规则引擎-数据转发'],
                    ['/basics-guide/course/device-alarm.md', '规则引擎-告警设置'],
                    ['/best-practices/rule-engine-http-server.md', '通过规则引擎发布http api服务(PRO)'],
                    ['/best-practices/rule-engine-device-avg-temp.md', '通过规则引擎计算1分钟内设备平均温度(PRO)'],
                    ['/best-practices/rule-engine-device-same-group.md', '当房间开门时,打开对应房间当空调(PRO)'],
                    ['/dev-guide/device-firmware', '设备固件更新'],
                    ['/advancement-guide/benchmark.md', '压力测试']
                ]
            },
            {
                title: '操作手册',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/basics-guide/course/system-layout', '系统配置'],
                    ['/basics-guide/device-manager.md', '设备管理'],
                    ['/basics-guide/course/network.md', '设备接入'],
                    ['/basics-guide/course/notification.md', '通知管理'],
                    ['/basics-guide/course/rule-engine-nodered.md', '规则引擎']
                    //['/basics-guide/course/logger.md', '日志管理']
                ]
            },
            {
                title: '开发手册',
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 1,    // 可选的, 默认值是 1
                children: [
                    ['/dev-guide/start.md', '介绍'],
                    ['/dev-guide/specification', '规范'],
                    ['/dev-guide/reactor.md', '响应式'],
                    ['/dev-guide/crud.md', '增删改查'],
                    ['/dev-guide/custom-sql-term', '自定义通用SQL条件'],
                    ['/dev-guide/event-driver.md', '事件驱动,消息总线'],
                    ['/dev-guide/reactor-ql.md', 'ReactorQL,使用SQL处理实时数据.'],
                    //['/dev-guide/utils', '常用API及工具类'],
                    ['/dev-guide/custom-message-protocol.md', '自定义消息协议'],
                   // ['/dev-guide/custom-notification-component.md', '自定义通知组件'],
                   // ['/dev-guide/send-message.md', '向设备发送消息'],
                    // ['/dev-guide/subscribe-device-message', '从事件总线中订阅消息'],
                    ['/dev-guide/websocket-subs', '使用websocket订阅消息'],
                    ['/dev-guide/multi-tenant', '多租户'],
                    //['/dev-guide/micro-service', '微服务']

                ]
            },
            {
                title: 'openApi',   // 必要的
                //      path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    //['', '介绍'],
                    //['', '快速开始'],
                    ['/interface-guide/open-api/access', '接入'],
                    ['/interface-guide/open-api/open-api', 'API列表'],
                ]
            },
            {
                title: '常见问题',   // 必要的
                //      path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 1,    // 可选的, 默认值是 1
                children: [
                    //['', '介绍'],
                    //['', '快速开始'],
                    ['/common-problems/install.md', '安装,启动常见问题'],
                    ['/common-problems/network-components.md', '网络组件常见问题'],
                    ['/common-problems/mqtt-connection.md', '使用MQTT接入时的常见问题'],
                    ['/common-problems/tcp-network-components.md', 'TCP网络组件常见问题']
                ]
            },
            // {
            //         title: 'HTTP接口文档',   // 必要的
            // //      path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
            //         collapsable: false, // 可选的, 默认值是 true,
            //         sidebarDepth: 1,    // 可选的, 默认值是 1
            //         children: [
            //             //['', '介绍'],
            //             //['', '快速开始'],
            //                 ['/interface-guide/authentication','权限设置'],
            //                 ['/interface-guide/device/device-product','设备型号'],
            // 	['/interface-guide/device/device-instance','设备实例'],
            //                 ['','网络组件'],
            //                 ['','设备接入'],
            //                 ['','消息通知'],
            //                 ['','openApi']
            //         ]
            //     }
            //  ,
            //  {
            //      title: '开发手册',
            //      collapsable: false, // 可选的, 默认值是 true,
            //      sidebarDepth: 1,    // 可选的, 默认值是 1
            //      children: [
            //          ['/dev-guide/start.md', '简介'],
            // ['/dev-guide/specification', '命名'],
            //  ['/dev-guide/crud.md', '增删改查'],
            // ['/dev-guide/device-operation.md', '设备操作'],
            // ['/dev-guide/event-driver.md', '事件驱动'],
            // ['/dev-guide/message-gateway.md', '消息网关'],
            // ['/dev-guide/device-gateway.md', '设备网关'],
            //          ['/dev-guide/jetlinks-protocol', '协议开发'],
            //          ['/dev-guide/rule-engine', '规则引擎']
            //      ]
            // }
        ]
    },
    // markdown: {
    //     // markdown-it-anchor 的选项
    //     anchor: {permalink: false},
    //     // markdown-it-toc 的选项
    //     toc: {includeLevel: [1, 2]},
    //     extendMarkdown: md => {
    //         // 使用更多的 markdown-it 插件!
    //         md.use(require('markdown-it-checkbox'),{
    //             disabled: true,
    //             divWrap: false,
    //             divClass: 'checkbox',
    //             idPrefix: 'cbx_',
    //             ulClass: 'task-list',
    //             liClass: 'task-list-item'
    //         });
    //         md.render('- [ ] unchecked');
    //         md.render('- [x] checked');
    //     }
    // }
//     plugins: {
//         '@vuepress/medium-zoom': {
//             //selector: 'img',
//             // medium-zoom options here
//             // See: https://github.com/francoischalifour/medium-zoom#options
//             options: {
//                 margin: 16
//             }
//
//         }
//     }
    plugins:[

        [
            'vuepress-plugin-medium-zoom',
            {
                //selector: 'img',
                options: {
                    margin: 16
                }
            }
        ]

    ]
 }

