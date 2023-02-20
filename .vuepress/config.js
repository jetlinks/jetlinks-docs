module.exports = {
    title: 'JetLinks 物联网基础平台（2.x）',
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
            {text: '1.0文档', link: 'https://doc.v1.jetlinks.cn/'},
            {text: '关于', link: 'http://jetlinks.cn/'},
            {text: 'GitHub', link: 'https://github.com/jetlinks'},
            {text: 'gitee', link: 'https://gitee.com/jetlinks'},
            {text: '提交问题', link: 'https://github.com/jetlinks/jetlinks-community/issues'},
            {text: '文档纠错', link: 'https://github.com/jetlinks/jetlinks-docs/issues'}
        ],
        sidebar: [
            {
                title: '首页',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/', '介绍'],
                    // ['/home/Version_upgrade.md', '版本升级说明'],
                    ['/home/update-log.md', '更新记录'],
                    ['/home/product-migration.md', '产品迁移'],
                ]
            },
            {
                title: '产品简介',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/Product_introduction/Product_introduction2.1.md', '产品简介'],
                    ['/Product_introduction/Basic_concepts2.2.md', '基本概念'],
                        ]
            },
            {
                title: '安装部署',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/install-deployment/deploy-community.md', '社区版（community）'],
                    ['/install-deployment/deploy-pro.md', '企业版（pro）'],
                    ['/install-deployment/deploy-cloud.md', '微服务版（cloud）'],
                    // ['/install-deployment/deploy-community.md', '本地源码启动'],
                    // ['/install-deployment/docker-start.md', '使用docker启动'],
                    // ['/install-deployment/ide-docker-start.md', '开发环境最佳实践'],
                    // ['/install-deployment/jetlinks-cloud.md', '启动jetlinks微服务版本'],
                    ['/dev-guide/java-deploy.md', '部署到服务器'],
                    // ['/install-deployment/deployment.md', '部署到服务器'],
                    // ['/install-deployment/cluster.md', '集群部署'],
                    ['/dev-guide/colony-deploy.md', '集群部署'],
                    ['/install-deployment/performance.md', '性能优化'],
                    // ['/install-deployment/enterprise-version-start.md', '企业版源码启动'],
                    ['/install-deployment/enterprise-version-storehouse.md', '企业版代码仓库说明']
                ]
            },
            {
                title: '设备接入',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [ 
                    ['/Device_access/Create_product3.1.md', '创建产品'],
                    ['/Device_access/Create_Device3.2.md', '创建设备'],
                    ['/Device_access/Create_gateways_and_sub_devices3.3.md', '网关与子设备'],
                    ['/Device_access/Configuration_model3.4.md', '配置物模型'],
                    ['/Device_access/Device_access3.5.md', '设备接入方式'],
                                    ]
            },
             
            {
                title: '操作手册',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/device_management/product4.1', '设备管理'],
                    ['/Mocha_ITOM/Device_access_gateway5.1.md', '运维管理'],
                    ['/Alarm_Center/Alarm_configuration6.1.md', '告警中心'],
                    ['/Notification_management/Notification_management7.md', '通知管理'],
                    ['/Northbound_output/Northbound_output8.md', '北向输出'],
                    ['/Rule_engine/Rule engine9.md', '规则引擎'],
                    ['/Video_Center/Video_equipment10.md', '视频中心'],
                    ['/Data_Acquisition/Data_Acquisition.md', '数采管理'],
                    ['/Iot_Card/Card.md', '物联卡'],
                    ['/System_settings/System_settings.md', '系统设置'],
                    ['/Personal_Center/Personal_Center.md', '个人中心'],
                ]
            },
            {
                title: '功能说明',   // 必要的
                //	path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/function-description/common_expression.md', '常见名词说明'],
                    ['/function-description/metadata_description.md', '物模型说明'],
                    ['/function-description/device_message_description.md', '平台统一设备消息定义'],
                    ['/function-description/storage_policy.md', '存储策略选择'],
                    ['/function-description/data-forwarding.md', '平台设备数据转发'],
                ]
            },
            {
                title: '最佳实践',
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children:[
                    ['/Best_practices/Device_access.md', '设备接入'],
                    //['/Best_practices/data-parse.md', '数据解析'],
                    ['/Best_practices/Scene_linkage.md', '场景联动'],
                    ['/Best_practices/Alarm_Center.md', '告警中心'],
                    // ['/Best_practices/Rule_engine.md', '规则引擎'],
                    // ['/Best_practices/Northbound_output.md', '北向输出'],
                    ['/Best_practices/Notification_management.md', '通知管理'],
                    // ['/Best_practices/National_standard_cascade.md', '国标级联'],
                    ['/Best_practices/application_management.md', '应用管理'],
                ]
            },
            {
                title: '设备接入协议开发手册',
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children:[
                    ['/protocol/start.md', '介绍'],
                    ['/protocol/first.md', '快速开始'],
                    ['/protocol/mqtt.md', 'MQTT协议解析'],
                    // ['/protocol/api.md', '核心类API说明'],
                    ['/protocol/faq.md', '常见问题']
                ]
            },
            {
                title: '平台开发手册',
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    ['/dev-guide/start.md', '介绍'],
                    ['/dev-guide/specification', '开发规范'],
                    ['/dev-guide/reactor.md', '响应式'],
                    ['/dev-guide/crud.md', '增删改查'],
                    ['/dev-guide/custom-sql-term.md', '自定义通用SQL条件'],
                    ['/dev-guide/event-driver.md', '事件驱动,消息总线'],
                    ['/dev-guide/reactor-ql.md', 'ReactorQL,使用SQL处理实时数据.'],
                    ['/dev-guide/rule-engine.md', '规则引擎说明'],
                    //['/dev-guide/utils', '常用API及工具类'],
                    // ['/dev-guide/protocol-support.md', '协议开发说明'],
                    ['/dev-guide/jetlinks-protocol-support.md', 'JetLinks 官方协议'],
                    ['/dev-guide/jetlinks-transparent.md', '透传协议说明'],
                    ['/dev-guide/custom-message-protocol.md', '自定义消息协议'],
                   // ['/dev-guide/custom-notification-component.md', '自定义通知组件'],
                   // ['/dev-guide/send-message.md', '向设备发送消息'],
                    // ['/dev-guide/subscribe-device-message', '从事件总线中订阅消息'],
                    // ['/dev-guide/websocket-subs.md', '使用websocket订阅平台消息'],
                    // ['/dev-guide/mqtt-subs.md', '使用mqtt订阅平台消息'],
                    ['/dev-guide/commons-api.md', '平台内部核心类及接口说明'],
                    ['/dev-guide/cluster.md', '集群管理'],
                    ['/dev-guide/assets.md', '数据权限控制'],
                    ['/dev-guide/sso.md', '单点登录'],
                    ['/dev-guide/i18n.md', '国际化'],
                    ['/dev-guide/code-guide.md', '开发及使用指导'],
                    //['/dev-guide/micro-service', '微服务']
                ]
            },
            {
                title: '视频中心',   // 必要的
                //      path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2,    // 可选的, 默认值是 1
                children: [
                    //['', '介绍'],
                    //['', '快速开始'],
                    ['/media-guide/media_basic.md', '快速入门'],
                    ['/media-guide/media_access_process.md', '视频接入流程'],
                    ['/media-guide/media-base-config.md', '配置摄像头示例'],
                ]
            },
            // {
            //     title: '应用管理',   // 必要的
            //     //      path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
            //     collapsable: false, // 可选的, 默认值是 true,
            //     sidebarDepth: 2,    // 可选的, 默认值是 1
            //     children: [
            //         //['', '介绍'],
            //         //['', '快速开始'],
            //         ['/interface-guide/application/api-server.md', 'API服务']
            //     ]
            // },
            {
                title: '常见问题',   // 必要的
                //      path: '/basics-guide/course',      // 可选的, 应该是一个绝对路径
                collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 1,    // 可选的, 默认值是 1
                children: [
                    //['', '介绍'],
                    //['', '快速开始'],
                    ['/common-problems/install.md', '安装,启动常见问题'],
                    // ['/common-problems/network-components.md', '网络组件常见问题'],
                    ['/protocol/faq.md', '协议开发常见问题'],
                    ['/common-problems/mqtt-connection.md', '使用MQTT接入时的常见问题'],
                    ['/common-problems/tcp-network-components.md', 'TCP网络组件常见问题'],
                    ['/common-problems/tenant.md', '平台怎样支持"租户"概念问题'],
                    ['/common-problems/FAQ.md', '其他常见问题']
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
            //                 ['/interface-guide/device/device-product','产品'],
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

