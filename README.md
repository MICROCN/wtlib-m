[Library]----just for 8302

## 介绍
此次项目开发的自助化图书借阅推荐系统基于微信公众号上运营，它不仅可以帮助管理员查看书籍的借阅情况以便购买更多的此类书籍，还可以方便用户去了解图书馆藏的分布，及时了解某书籍的借阅和预定情况，在用户看完书以后可以在系统上对此书进行评价、贴上代表书评的标签、分享相关书籍的链接和构建专业知识树等一些功能。此项目的自助化图书借阅推荐系统会根据书籍的预约情况在馆藏图书可供借阅时及时通知所有预定者前来借书，也可以根据图书借阅情况为用户推荐相关书籍。当然，在完成基础的图书借阅等功能后，我们亦会陆续推出自助化图书借阅推荐系统的其他扩展功能，比如在线浏览书籍，微课堂，图书分享系统等，这将对于学生和教师或者其他热爱读书的用户来说会更加方便操作。
## 技术关键
1. 后端采用javaweb技术进行界面交互、逻辑处理与存储。
2. 前端html5、css、js、jquery、bootstrap用于界面展示。
3. 服务端以Nginx作为反向代理静态服务器，tomcat作为后端服务器，Postfix,Dovecot作为邮件服务器。
4. 数据库端采用mysql与redis，其中mysql作为普通的存储数据，redis作为缓存。
5. python爬虫用于获取已有的图书管理系统的数据抓取。
6. rabbitMQ用于模块通信，主要是用来实现应用程序的异步和解耦，同时也能起到消息缓冲，消息分发的作用。
7. mahout用于推荐算法，根据用户兴趣喜好或者个性标签来进行推荐书籍。
## 角色以及权限
###### 按照角色分为图书管理员，普通借书用户
权限 | 图书管理员 | 普通借书用户
:----:|:---------:|:-----------:
借书还书预定书籍|/|/
上传修改删除书籍|/|n
查看书籍借阅记录|/|/
修改信息（含密码）|/|/
增加删除修改用户（含密码）|/|n
添加书籍标签|/|/
删除修改书籍标签|/|n
查看书籍标签|/|/
添加修改删除用户积分权重|/|n
查看用户积分详情|/|/
兑换积分|/|/
