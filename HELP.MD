## 权限管理模型

1. 用户：发起操作的主体;
2. 对象：指操作所针对的客体对象，比如订单数据或图片文件；
3. 角色：赋予用户某种身份；
4. 权限控制表(ACL: Access Control List)：用来描述权限规则或用户和权限之间关系的数据表；
5. 权限(Permission)：用来指代对某种对象的某一种操作，例如“添加文章的操作”；
6. 权限标识：权限的代号，例如用“ARTICLE_ADD”来指代“添加文章”的操作权限。

#### 自主访问控制(DAC: Discretionary Access Control)

自主访问控制(DAC: Discretionary Access Control)
用户(User) 操作对象(Subject) 权限控制列表 (ACL: Access Control List),
对象本身拥有哪些权限列表,来决定用户能操作哪些,所以对于某些用户都有的权限来说操作比较麻烦,因为需要这些文件都有这个权限列表

#### 强制访问控制模型(MAC: Mandatory Access Control)

MAC 是为了弥补 DAC 权限控制过于分散的问题而诞生的。在 MAC
的设计中，每一个对象都有一些权限标识，每个用户同样也会有一些权限标识，而用户能否对该对象进行操作取决于双方权限标识的关系，这个关系的判断通常是由系统硬性限制的。比如在影视作品中我们经常能看到特工在查询机密文件时，屏幕提示“无法访问，需要一级安全许可”。这个例子中，文件上就有“一级安全许可”的权限标识，而用户并不具有。

#### RBAC

## 模型实现框架- Spring-Security

Spring Security过滤器
SecurityContextPersistenceFilter
LogoutFilter
AbstractAuthenticationProcessingFilter
DefaultLoginPageGeneratingFilter
BasicAuthenticationFilter
SecurityContextHolderAwareRequestFilter
RememberedAuthenticationFilter
AnonymousAuthenticationFilter
ExceptionTranslationFilter
SessionManagementFilter
FilterSecurityInterceptor
FilterChainProxy


## 代码优化
1. 树形结构优化(菜单类优化)
2. 缓存优化(redis优化,命名规范化)
3. 登录优化JWT(利用jwt)
4. 日志优化(优化Aop,ValidationUtils校验优化)
5. 业务代码优化()
6. 工具类优化(部分工具类优化,如PasswordUtil) | 引用HuTool工具包替换掉
7. wx-common 工具类引用,因为wx-common 工具类处理异常
8. 包命名优化
9. 静态页面优化
