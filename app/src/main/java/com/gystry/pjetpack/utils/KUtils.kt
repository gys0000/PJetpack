package com.gystry.pjetpack.utils

import com.gystry.pjetpack.recyclerview.User

/**
 * @author gystry
 * 创建日期：2020/12/15 20
 * 邮箱：gystry@163.com
 * 描述：
 */

fun getUserList(): ArrayList<User> {
    val userList = ArrayList<User>()
    userList.add(User("潘石屹", 12, "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3512008785,3293619463&fm=26&gp=0.jpg", "以梦为鹿，亡与桎梏。", VIEW_TYPE_STUDENT))
    userList.add(User("广告", 0, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608037956984&di=cc85f4b09d276bc1aef69138e8e81a57&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2Fu%2F00%2F24%2F84%2F32%2F5617741c378a8.jpg", "", VIEW_TYPE_ADVERTISING))
    userList.add(User("牛根生", 13, "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=103094463,3142272961&fm=26&gp=0.jpg", "历经万般红尘劫，犹如凉风轻拂面。", VIEW_TYPE_STUDENT))
    userList.add(User("陈鸿宇", 12, "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2944814014,1130384657&fm=26&gp=0.jpg", "暮冬时烤雪，迟夏写长信，早春不过一棵树", VIEW_TYPE_STUDENT))
    userList.add(User("曾玉", 11, "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3656425730,837800821&fm=26&gp=0.jpg", "从不爱与忘怀之中，才能得到自由", VIEW_TYPE_STUDENT))
    userList.add(User("韩耀国", 45, "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3066595478,3917585433&fm=26&gp=0.jpg", "所谓无底深渊，下去也是前程万里。", VIEW_TYPE_TEACHER))
    userList.add(User("刘佳", 12, "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2453068193,3753699472&fm=26&gp=0.jpg", "心里的火永远不要灭，哪怕别人只能看到烟。", VIEW_TYPE_STUDENT))
    userList.add(User("张宁", 14, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608036193730&di=6ad3f4557bb2954f9f26c12c2bd57e0f&imgtype=0&src=http%3A%2F%2Fimage.suning.cn%2Fuimg%2Fsop%2Fcommodity%2F134955842771259028437100_x.jpg", "凌晨四点醒来，发现海棠花未眠。", VIEW_TYPE_STUDENT))
    userList.add(User("广告", 0, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608037956983&di=0b81af9e9dc3d61aef03493d9d253881&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F01%2F88%2F75%2Fa07b6faff5e101afef5c65e40106c825.jpg", "我的爱会攀上窗台盛放", VIEW_TYPE_ADVERTISING))
    userList.add(User("姚余梁", 12, "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2780416608,1897116386&fm=26&gp=0.jpg", "生活是一次伟大的失眠。", VIEW_TYPE_STUDENT))
    userList.add(User("马忠普", 12, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608036193717&di=156e87e7e3b2f0b6674a6dd9ea0be6a4&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn11%2F349%2Fw608h541%2F20181107%2F28db-hnprhzv7108365.jpg", "今天的天空像一封深蓝色的情书", VIEW_TYPE_STUDENT))
    userList.add(User("李馨", 13, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608036240737&di=c04737dc479fd2fa73e186514e36d548&imgtype=0&src=http%3A%2F%2Fimage.meifajie.com%2Fattachments%2Fimage%2F2019-04%2F20190411110427_99380.jpeg", "我太脆弱了，我就是一片海苔", VIEW_TYPE_STUDENT))
    userList.add(User("王长兴", 35, "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2615951445,351571368&fm=26&gp=0.jpg", "提及少年一词，应与平庸相斥。", VIEW_TYPE_TEACHER))
    userList.add(User("李泰龙", 26, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608036344978&di=b3a41826780c3d22974443d5581436c1&imgtype=0&src=http%3A%2F%2Fztd00.photos.bdimg.com%2Fztd%2Fw%3D700%3Bq%3D50%2Fsign%3D22aaa3ec8b8ba61edfeeca2f710fe637%2Fa8ec8a13632762d0649c0199a9ec08fa513dc672.jpg", "他无意掀翻烛火，点燃我双眸盛满的暮色。", VIEW_TYPE_TEACHER))
    userList.add(User("郭建昌", 13, "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2752364947,518172964&fm=26&gp=0.jpg", "我一个人前行，却仿佛带着一万雄兵。", VIEW_TYPE_STUDENT))
    userList.add(User("邢宝标", 13, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608036388260&di=af0f80e309c8a7a303fc2c7784a1c7ce&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20190729%2F75b48a13a8164b728aed6a05e07fda0f.jpeg", "我不想谋生，我想生活。", VIEW_TYPE_STUDENT))

    userList.add(User("扶苏", 12, "https://pic2.zhimg.com/80/v2-8061b6d86f9bbea27ca2c89abddb53c5_720w.jpg", "谁令碧海也变，变作浊流滔天。", VIEW_TYPE_STUDENT))
    userList.add(User("广告", 0, "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1768092885,618751527&fm=26&gp=0.jpg", "", VIEW_TYPE_ADVERTISING))
    userList.add(User("魏无忌", 13, "https://pic2.zhimg.com/80/v2-e31b97f2f19abe98f4ebbf7cb53c5081_720w.jpg", "如能忘掉渴望，岁月长衣裳薄。", VIEW_TYPE_STUDENT))
    userList.add(User("蓝忘机", 13, "https://pic2.zhimg.com/80/v2-1f2c39020ba3a0d3c478bc340ae7297d_720w.jpg", "应是天仙狂醉，乱把白云揉碎。", VIEW_TYPE_STUDENT))
    userList.add(User("郭嘉", 10, "https://pic4.zhimg.com/80/v2-57c85a13770489ecf7e32dc2c56964eb_720w.jpg", "留人间几回爱，迎浮生千重变", VIEW_TYPE_STUDENT))
    userList.add(User("李从善", 40, "https://pic2.zhimg.com/80/v2-8fe9a86b4e3c2a256d431df718b7c841_720w.jpg", "走在一条开满桃花的路上，\n" +
            "云蒸霞蔚，前途似锦。", VIEW_TYPE_TEACHER))
    userList.add(User("卓文君", 12, "https://pic4.zhimg.com/80/v2-66e031948c73fa9b19e36e02dee8bb87_720w.jpg", "春朝把芸苔煮了 晾在竹竿上 为夏天的粥。", VIEW_TYPE_STUDENT))
    userList.add(User("李商隐", 11, "https://pic4.zhimg.com/80/v2-908d43605bfab2db28f68c5b9e8a6483_720w.jpg", "胸中有丘壑，立马振山河。", VIEW_TYPE_STUDENT))
    userList.add(User("广告", 0,
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608043622793&di=5fc058bcd40f0fab0ef7f3803399ec43&imgtype=0&src=http%3A%2F%2Fimg.article.pchome.net%2F00%2F22%2F80%2F24%2Fpic_lib%2Fs960x639%2F2366sun017s960x639.jpg", "我的爱会攀上窗台盛放", VIEW_TYPE_ADVERTISING))
    userList.add(User("白敬亭", 12, "https://pic4.zhimg.com/80/v2-47829b6b2f4f5f39b8f9131448119286_720w.jpg?source=1940ef5c", "风月都好看，人间也浪漫。", VIEW_TYPE_STUDENT))
    userList.add(User("都云谏", 12, "https://pic1.zhimg.com/80/v2-be608d9600e1bf8aae8175a9dc2afef0_720w.jpg?source=1940ef5c", "你看这世界不坏，天塌了还有爱在", VIEW_TYPE_STUDENT))
    userList.add(User("南锦屏", 15, "https://pic2.zhimg.com/80/v2-37d4d2abe2edae9efb1de1f02f3c3bf5_720w.jpg?source=1940ef5c", "知命不惧，日日自新", VIEW_TYPE_STUDENT))
    userList.add(User("柳如是", 31, "https://pic3.zhimg.com/80/v2-e320b87b38e7a54bcbd443425b2ff816_720w.jpg", "提不是山谷。", VIEW_TYPE_TEACHER))
    userList.add(User("周归璨", 43, "https://pic1.zhimg.com/80/v2-f9bd0aaa0de1ee9eeddb74fd0bd61a6e_720w.jpg?source=1940ef5c", "你是我漫漫余生里义无反顾的梦想", VIEW_TYPE_TEACHER))
    userList.add(User("戴岳", 13, "https://pic3.zhimg.com/80/v2-33f6121e2ee46cc627735f00702d9756_720w.jpg", "须知少时凌云志 曾许人间第一流", VIEW_TYPE_STUDENT))
    userList.add(User("吴秋舫", 12, "https://pic4.zhimg.com/80/v2-317ef2538d7186ecaf78673b6cc4542b_720w.jpg", "你所见即我，我不辩解", VIEW_TYPE_STUDENT))
    return userList
}