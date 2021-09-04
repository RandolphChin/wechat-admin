#### 项目简介
基于[el-admin](https://el-admin.vip)项目
#### 添加的功能
 1. 美团 Leaf ID生成系统
 2. 定时任务quartz支持集群
 3. RabbitMq
 4. 微信小程序登录

微信小程序客户端前往这里[uniapp-template](https://github.com/RandolphChin/uniapp-template.git) https://github.com/RandolphChin/uniapp-template.git

#### 增加美团Leaf模块用于生成递增序列
IDGen.get("big_tag名称")
#### 添加支持多租户(租户字段)
使用 Dept 中的 id 和 Job 中的 id 分别作为租户ID，JOB与Dept是一对多关系；
新建一角色A，指定A可以自定义访问的dept集合；
用户User可以访问一个或多个Dept，用户User只能归属于一个Job，一个Job可以有多个Dept
用户名 许昌1号可以查看到 JOB为许昌，Dept为A区的数据
用户名 许昌2号可以查看到 JOB为许昌，Dept为B区的数据
用户名 许昌可以查看到 JOB为许昌，Dept为A、B两个区的数据

如何使用：所有数据表都需要添加 job_id 和 dept_id 租户字段
示例 testing表
```
id	    bigint					
dept_id	bigint		展区			
job_id	bigint		场馆			
name	varchar		姓名	

```
```java
@Data
@DataPermission(fieldName = "id",joinName = "dept")
@DataVenuePermission(fieldName = "id",joinName = "job")
public class TestingQueryCriteria{

}

@Entity
@Data
@Table(name="testing")
public class Testing implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @JoinColumn(name = "dept_id",referencedColumnName = "dept_id")
    @ManyToOne
    @ApiModelProperty(value = "展区")
    private Dept dept;

    @JoinColumn(name = "job_id",referencedColumnName = "job_id")
    @ManyToOne
    @ApiModelProperty(value = "场馆")
    private Job job;

    @Column(name = "name")
    @ApiModelProperty(value = "姓名")
    private String name;

    public void copy(Testing source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
```
```sql
CREATE TABLE `leaf_alloc` (
  `biz_tag` varchar(128)  NOT NULL DEFAULT '', -- your biz unique name
  `max_id` bigint(20) NOT NULL DEFAULT '1',
  `step` int(11) NOT NULL,
  `description` varchar(256)  DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`biz_tag`)
) ENGINE=InnoDB;

insert into leaf_alloc(biz_tag, max_id, step, description) values('leaf-segment-test', 1, 2000, 'Test leaf Segment Mode Get Id')
```
### rabbitmq 部署
#### 1st make Dockerfile
```
FROM rabbitmq:3-management-alpine
RUN apk add curl
RUN curl -L https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/3.8.17/rabbitmq_delayed_message_exchange-3.8.17.8f537ac.ez> $RABBITMQ_HOME/plugins/rabbitmq_delayed_message_exchange-3.8.17.8f537ac.ez 
RUN chown rabbitmq:rabbitmq $RABBITMQ_HOME/plugins/rabbitmq_delayed_message_exchange-3.8.17.8f537ac.ez
RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange
RUN rabbitmq-plugins enable --offline rabbitmq_consistent_hash_exchange
```
#### 2nd build rabbitmq
>docker build -t rabbitmq:3-management-alpine .

#### 3rd run rabbitmq
```
docker run -d --hostname rabbit --name rabbitmq -p 4369:4369 -p 5672:5672 -p 15672:15672 -p 1883:1883 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -v E:\docker-link\rabbitmq\data\rabbitmq1:/var/lib/rabbitmq/mnesia rabbitmq:3-management-alpine
```
