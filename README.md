

#### 介绍
​	基于Spring AOP对Mybatis Plus的进一步增强，让你一键完成业务代码（吹牛）！

> 注意：
>
> Mybatis Plus版本要求：3.4.0
>
> Spring AOP版本要求：2.2.1.RELEASE
>
> 其他版本没试过




#### 安装教程

1.  clone本项目到本地
2.  然后导入到自己项目的模块中去



#### 核心功能

##### 自动化注解

​	假设有这样的一张表结构

```sql
CREATE TABLE `t_student`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名字',
  `age` int(3) NOT NULL COMMENT '年龄',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `nice_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

```



​	使用自动化注解一键完成业务操作

- @AutomationInsert : 自动插入
- @AutomationDelete : 自动删除
- @AutomationUpdate : 自动更新

- @AutomationSelectList : 自动查询列表

- @AutomationSelectExist : 自动查询是否已存在

- @AutomationSelectSingle : 自动查询单个对象



​	如下：

```java
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    /**
     * 添加数据
     * @param object 数据
     * @return 失败返回null
     */
    @AutomationInsert
    @Override
    public Student add(Student object) {
		return null;
    }


    /**
     * 删除信息
     * @param id 主键
     * @return 成功返回true，反之false
     */
    @AutomationDelete
    @Override
    public boolean remove(String id) {
		return false;
	}

    /**
     * 更新信息
     * @param object 数据
     * @return 成功返回true，反之false
     */
	@AutomationUpdate
    @Override
    public boolean update(Student object) {
		return false;
	}

    /**
     * 获得列表
     *
     * @param pageParam       分页参数
     * @param studentCriteria
     * @return 返回分页列表
     */
    @AutomationSelectList
    @Override
    public IPage<Student> getList(PageParam<Student> pageParam, StudentCriteria studentCriteria) {
        return null;
    }

    /**
     * 查询数据是否存在
     * @param id 主键
     * @return 存在返回true，反之false
     */
	@AutomationSelectExist
    @Override
    public boolean isExistById(String id) {

		return false;

	}

    /**
     * 获得单个数据
     * @param id 主键
     * @return 返回单个数据
     */
	@AutomationSelectSingle
    @Override
    public Student get(String id) {

		return null;

	}
}
```





##### 代码生成器

​	和Mybatis Plus的代码生成器一样，不同的是，我自定义了模板。可以直接生成带自动化注解的Service和controller层代码

可直接运行guhong.play.mybatisplusenhancer.generator的MyBatisCodeGenerator查看效果

​	下面是用自定义模板生成的代码



IService代码

```java

@Validated
public interface IStudentService extends IService<Student>, WholeVerifyHandler<Student, IStudentService> {


    /**
     * 添加数据
     *
     * @param object 数据
     * @return 失败返回null
     */
    public Student add(Student object);


    /**
     * 删除信息
     *
     * @param id 主键
     * @return 成功返回true，反之false
     */
    public boolean remove(String id);

    /**
     * 更新信息
     *
     * @param object 数据
     * @return 成功返回true，反之false
     */
    public boolean update(Student object);

    /**
     * 获得列表
     *
     * @param pageParam 分页参数
     * @return 返回分页列表
     */
    public IPage<Student> getList(PageParam<Student> pageParam);


    /**
     * 查询数据是否存在
     *
     * @param id 主键
     * @return 存在返回true，反之false
     */
    public boolean isExistById(String id);

    /**
     * 获得单个数据
     *
     * @param id 主键
     * @return 返回单个数据
     */
    public Student get(String id);


}

```





ServiceImpl代码

```java

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    private LoginInfoService loginInfoService;

    /**
     * 添加数据
     * @param object 数据
     * @return 失败返回null
     */
    @AutomationInsert
    @Override
    public Student add(Student object) {
		return null;
    }


    /**
     * 删除信息
     * @param id 主键
     * @return 成功返回true，反之false
     */
    @AutomationDelete
    @Override
    public boolean remove(String id) {
		return false;
	}

    /**
     * 更新信息
     * @param object 数据
     * @return 成功返回true，反之false
     */
	@AutomationUpdate
    @Override
    public boolean update(Student object) {
		return false;
	}

  

    /**
     * 查询数据是否存在
     * @param id 主键
     * @return 存在返回true，反之false
     */
	@AutomationSelectExist
    @Override
    public boolean isExistById(String id) {

		return false;

	}

    /**
     * 获得单个数据
     * @param id 主键
     * @return 返回单个数据
     */
	@AutomationSelectSingle
    @Override
    public Student get(String id) {

		return null;

	}



    /**
     * 分页查询
     * @param pageParam 分页参数
     * @return 返回分页列表
     */
    @AutomationSelectList
    @Override
    public IPage<Student> getList(PageParam<Student> pageParam) {
        return null;
    }

    /**
     * 添加验证
     *
     * @param object  数据对象
     * @param service 对应的service
     */
	@Validated(value = {ValidatorGroup.Insert.class})
    @Override
    public void insertVerify(@Valid Student object, IStudentService service) {

    }





    /**
     * 更新验证
     *
     * @param object  数据对象
     * @param service 对应的service
     */
    @Validated(value = {ValidatorGroup.Update.class})
    @Override
    public void updateVerify(@Valid Student object, IStudentService service) {

    }




}

```



Controller代码

```java

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2023-06-28
 */
@RestController
@RequestMapping("/student")
@Api(value = "", tags = "")
public class StudentController extends BaseController {

    @Resource
    private IStudentService mainService;

    @ApiOperation(value = "添加【学生】数据", response = Result.class)
    @PostMapping("/add")
    public Result add(Student entity) {

        Student addResult = mainService.add(entity);
        return super.successOrError("addResult");

    }

    @ApiOperation(value = "根据id删除【学生】数据", response = Result.class)
    @DeleteMapping("/remove")
    public Result remove(@RequestParam(name = "id") String id) {

        return super.successOrError(mainService.remove(id));

    }


    @ApiOperation(value = "根据id修改【学生】数据", response = Result.class)
    @PutMapping("/update")
    public Result update(Student entity) {

        return super.successOrError(mainService.update(entity));

    }

    @ApiOperation(value = "查询【学生】数据", response = Result.class)
    @GetMapping("/list")
    public Result list(PageParam<Student> pageParam) {

        return null;
    }


    @ApiOperation(value = "根据id判断【学生】数据是否存在", response = Result.class)
    @GetMapping("/exist")
    public Result isExist(@RequestParam(name = "id") String id) {

        return super.success(mainService.isExistById(id));

    }

    @ApiOperation(value = "根据id获得【学生】数据", response = Result.class)
    @GetMapping("/get")
    public Result get(@RequestParam(name = "id") String id) {

        return super.successOrEmpty(mainService.get(id));

    }
}

```



#### 高级特性

##### 不同的执行时机

在每个注解中，都包含一个名为executionTime的属性，用于表示增强操作的执行时机。该属性是一个枚举类型，具有两个取值：BEFORE和AFTER，默认取值为BEFORE。例如：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps13.jpg) 

 

默认情况下，executionTime属性被设置为BEFORE，这意味着增强处理在原始方法执行之前执行。因此，最终的执行效果是先执行增强处理，即添加学生信息，然后执行原始方法，即初始化成绩信息。

 

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps14.jpg) 

 

将executionTime属性设置为AFTER，表示启用后置增强，即增强处理在原始方法执行之后执行。因此，最终的执行效果是先执行原始方法，即添加账号信息，然后执行增强处理，即添加学生信息。

 

通过设置executionTime属性，我们可以轻松控制增强处理的时机，从而在不同的业务场景中实现灵活的配合。这种灵活性提高了代码的可扩展性，使我们能够根据需求调整增强处理的顺序和时机。

 

##### 结果处理器

由于不同项目甚至不同方法的返回类型可能不同，增强处理过程中难以构建返回结果。因此，我们需要引入结果处理器（ResultHandler）来处理增强后的返回结果。

和executionTime类似，每个注解上都有一个resultHandler属性，用于指定一个结果处理器。结果处理器（ResultHandler）负责接收增强后的返回结果，并进行相应的处理。它可以根据具体需求来定义，用于解析、转换、过滤或其他操作增强后的返回结果。

基于单一性原则，六种不同的操作对应了六种不同的ResultHandler。

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps15.jpg) 

 

同时，为了方便使用，我们也提供了默认的实现：DefaultResultHandler

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps16.jpg) 

 

DefaultResultHandler实现了WholeResultHandler接口，而WholeResultHandler接口继承了所有ResultHandler。他们关系如下：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps17.jpg) 

 

根据实际项目需求，也可以自行实现一套ResultHandler来满足不同场景下的不同要求。



##### 入参处理器

inParamHandler()表示一个入参处理器（InParamHandler），它仅在@AutomationInsert和@AutomationUpdate注解中存在，这两个注解对应插入和更新操作。这是因为在插入或更新操作中，通常需要传入一个对象或参数来处理数据库操作。而不同项目和开发人员有不同的开发习惯和风格。

 

有些开发人员喜欢将实体类的字段拆分到接口的入参中，例如：update(String name, String id, Integer sex, String address)。

有些人喜欢直接使用实体类作为入参，例如：update(User user)。

还有些人喜欢使用参数传递对象（DTO）来处理传参，最终将该对象转换为实体类，例如：update(UserDTO userDto)。

为了适应不同的入参方式，我们引入了入参处理器（InParamHandler）。通过使用InParamHandler，我们可以将入参转换为需要操作的实体类，以便后续进行插入或更新操作。

 

基于单一性原则，插入和更新操作对应了两种不同的InParamHandler，分别是：InsertInParamHandler和UpdateInParamHandler。它们的逻辑完全一样，只是名字做了区分。以InsertInParamHandler为例：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps18.jpg) 

 

findOriginalInParamForInsert方法会根据不同特性的入参找到用于插入的原始入参数据。

toInsertEntityObject方法会将findOriginalInParamForInsert方法找到的入参进行转换，转换成用于添加的实体类。



为了方便使用，针对常见的入参形式，在guhong.play.mybatisplusenhancer.handler.param.impl包下提供了他们对应的InParamHandler。

 

- DTOInParamHandler用于处理DTO入参。
- EntityInParamHandler用于处理实体类入参。
- MoreParamInParamHandler用于处理拆分形式的入参。
- ListDTOInParamHandler用于处理List<DTO>入参，适用于批量插入的场景
- MoreParamToEntityParamHandler继承与MoreParamInParamHandler，适用于入参是拆分形式，但验证处理器验证的是一个实体类的特殊场景。

 

这些InParamHandler实现了WholeInParamHandler接口，而

WholeInParamHandler接口同时实现了InsertInParamHandler和UpdateInParamHandler。因为一般情况下，插入操作的入参和更新操作的入参都是采用同一种方式。所以这些实现类中，只实现了插入操作的处理，而更新操作直接调用了插入操作的方法。以MoreParamInParamHandler为例：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps19.jpg) 

 

 

他们的关系如下：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps20.jpg) 

 

同ResultHandler一样，你也可以根据需要自行实现对应的InParamHandler来满足不同场景下的不同需求。



##### 验证处理器

verifyHandler()表示一个验证处理器，和入参处理器一样，验证处理器（VerifyHandler）仅在@AutomationInsert和@AutomationUpdate注解中存在。这是因为在插入或更新操作前，通常需要对入参的数据进行验证，比如：非空验证、长度验证、唯一性验证等等。

基于单一性原则，插入和更新操作对应了两种不同的VerifyHandler，分别是：InsertVerifyHandler和UpdateVerifyHandler。这样的设计一方面使得我们能够灵活适配不同的验证方式，另一方面让验证相关的代码从业务代码中分离出来，从而更加符合一个方法只做一件事的原则。

 

实际上，在执行插入和更新的增强时，验证部分可以分为三块：基础验证、自定义验证和通用验证。

基础验证是指通过javax.validation.constraints包中的验证注解完成的验证，如：@Max、@NotBlank、@Email等。

自定义验证就是执行具体的验证处理器（VerifyHandler）完成的验证。

而通用验证是指一些在验证逻辑中通用的验证，如：唯一性验证和关联验证。唯一性验证是指某个值在某个数据表中是否唯一，而关联验证表示某个数据在其关联的数据表中是还否存在或可用。

 

在实际使用中，基础验证可以通过javax.validation.constraints包中的验证注解并配合@Validator或@Valid注解完成验证。如：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps21.jpg) 

 

自定义验证这块因为不同的业务有着完全不同的验证逻辑，所以这部分需要开发人员根据实际情况去实现guhong.play.mybatisplusenhancer.handler.verify包下的InsertVerifyHandler或UpdateVerifyHandler接口。它们的逻辑完全一样，只是名字上做了区分。以InsertVerifyHandler为例：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps22.jpg) 

 

insertVerify方法接收两个泛型：V和S。V表示要验证的对象，S表示IService实例。需要注意的是，这里的V需要和InsertInParamHandler的findOriginalInParamForInsert方法获得的对象是相同类型。因为在增强时，会将InsertInParamHandler找到的原始入参当做验证对象传入InsertVerifyHandler进行验证。如果两者不一致，则会抛出IllegalArgumentException异常。

如果不需要自定义验证也可以直接用verifyHandler()默认属性：DefaultVerifyHandler，在DefaultVerifyHandler的实现中不会执行任何操作。

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps23.jpg) 

 

而通用验证可以使用guhong.play.mybatisplusenhancer.annotation.verify包下的关联验证注解（@RelationVerify）和唯一验证注解（@UniqueVerify）。只需要将其标识在需要验证的字段上即可。在执行AOP增强时，会通过guhong.play.mybatisplusenhancer.util下的VerifyUtil类去执行验证操作。

当然，如果你没有使用AOP增强，也可以直接使用VerifyUtil类配合注解的方式完成通用验证

 

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps24.jpg) 

 

@RelationVerify注解有三个属性：relationPrimaryName表示关联的主键名称，默认为id。relationService表示关联的IService类。errorMessage表示验证出错时抛出的异常信息，如果为空，系统会给出默认的提示。



![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps25.jpg)

 

@UniqueVerify注解有两个属性：columns表示要验证的列名，默认会以被注解的字段名作为要验证的列名。errorMessage表示验证出错时抛出的异常信息，如果为空，系统会给出默认的提示。

当@UniqueVerify用于类的某个字段上时，表示验证单个字段的唯一性。当@UniqueVerify直接标注在类上时，则可以表示多个字段同时参与唯一性验证。



##### 自动注入service

在每种切点注解中，都会需要指定一个service属性，表示要使用哪个IService去执行具体的操作。实际代码中，切点注解使用的地方往往就是其service指定的具体IService。

例如下面的代码，在StudentServiceImpl类中使用@AutomationInsert注解，并且需要指定service属性为StudentServiceImpl。表示使用StudentServiceImpl作为具体的服务实现类。

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps26.jpg) 

 

虽然只是加了个service属性，但实际使用起来却显得特别的臃肿和多于。为了让注解的使用变得简单且优雅,在guhong.play.mybatisplusenhancer.base.service目录中提供了一个名为SelfServiceImpl的实现类，它实现了Mybatis Plus的IService。但这个类什么都不会做，只是一个标识。表示是Service就是当前使用注解的类。

当读取到SelfServiceImpl时，我们便会通过反射技术找到对应的Class，并从Spring中获取到具体的实例，从而自动推断出具体的IService实例。

 

这就是为什么所有切点注解中service属性默认是一个SelfServiceImpl的原因。在默认情况下，我们会根据切点注解所在的类来自动推断使用的IService实例。

但需要注意，在非IService的实现类中使用切点注解时，系统无法自动推断具体的IService实例。这个时候必须明确指定service属性来确保正确的服务实例被使用。如果未指定service属性或指定的service属性与实际的实例不匹配，系统将会抛出WrongServiceException异常，以提醒开发者进行修正。



##### 查询的高阶使用

在实际业务中，查询操作呈现出多样性的特点。为了满足这种需求，我们也基于 MyBatis Plus 的框架进行了增强。

 

 

###### 分页查询

如果需要使用分页查询，只需要在查询方法的入参中加入guhong.play.mybatisplusenhancer.base中的PageParam对象即可。例如：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps27.jpg) 

在PageParam类中可以指定当前页码、每页限制条数以及排序等数据。

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps28.jpg) 

 

注意，在Mybatis Plus中使用分页查询功能需要自行配置分页插件（PaginationInnerInterceptor）。如：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps29.jpg) 



###### 条件注解

在查询操作的过程中，经常需要用到各种条件表达式对数据进行筛选。比如等于（=），大于（>），小于等于（<=）或模糊匹配（like）等等。对于这方面，在guhong.play.mybatisplusenhancer.annotation.criteria包中我们提供了一系列的注解来对应不同的条件表达式。

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps30.jpg) 

 

比如：@Eq表示等于（=），@Ge表示大于等于（>=），@Like表示全模糊匹配（like ‘%?%’），@LikeLeft表示左模糊匹配（like ‘%?’）等。

 

这些注解的属性基本相同，以@Eq为例：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps31.jpg) 

columnNames属性表示条件列的名称，默认情况下会以被注解的入参或字段的名称作为条件列名。

tableName属性表示条件列所在的表名，默认会根据IService实例来获取对应的表名。

or属性表示是否启用OR表达式，可以与columnNames属性配合使用，实现多个条件列匹配同一个值的条件查询。

 

这些条件注解可以用于方法的入参和类的字段。下面是一个入参中使用条件注解的例子：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps32.jpg) 

 

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps33.jpg) 

 

最终执行的SQL如下：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps34.jpg) 



如果一个方法的入参太多，方法定义和使用就会变得臃肿和繁琐。所以，在面多很多入参作为条件列时，我们可以选择把这些条件列放到一个条件类中统一管理。像这样：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps35.jpg) 

 

StudentCriteria类实现了Criteria接口。这个接口只是一个标识，只要实现Criteria接口，在构建条件时，我们就会把这个类当做一个条件类，并逐个解析其每个字段上的条件注解。

下面是一个使用条件类的例子：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps36.jpg) 

 

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps37.jpg) 

最终执行的SQL如下：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps39.jpg) 

###### 复杂查询

在 @AutomationSelectList 和 @AutomationSelectSingle 注解中，引入了一个名为voClass的属性。该属性用于指定列表查询或详情查询时返回的视图对象（View Object）类型。当 voClass 的值不是 Object.class 时，将会启用复杂查询功能。

 

开启复杂查询后，程序会通过反射获得voClass的字段，然后将其组成SQL语句。同时，你还可以使用guhong.play.mybatisplusenhancer.annotation.relation包下的一系列注解实现更复杂的SQL。

- 用@Column注解指定列名。使用该注解后会自动以字段名作为这个列的别名。这样可以不必依赖bean和数据库的字段名一样。但还是能一样就一样
- 用@As注解设置查询列的别名。和@Column注解互斥，当使用@Column时@As不生效
- 用@SubQuery注解增加一个子查询
- 用@Join注解设置连表查询。

 

最后在配合条件注解就可以快速实现查询功能，而不需要写任何的SQL和业务代码。

例如，一个学生列表查询接口：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps40.jpg) 

 

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps41.jpg) 

 

最终执行出来的SQL为：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps42.jpg) 

 

需要注意的是，虽然使用注解可以轻松构建复杂的查询SQL，但我不建议这么去做。因为这会使代码逻辑变得无比混乱，无法直观的体现出SQL的本意。所以，在处理复杂的SQL时，我建议采用Mybatis原始的方法，将SQL写在Mapper.xml里面，从而保证SQL的简洁和直观。



#### 其他功能



##### “SuperEntity”对象

在编写业务代码时，我们或许会碰到这样的场景。

- 根据某张表的id，获取该表中的某些列。

- 根据某张表的id，修改该表中的某些列。

 

对于这种情况，如果我们使用Mybatis Plus自带的getOne或updateById方法就需要这样写，以getOne为例：

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps1.jpg) 

 

可以发现，仅仅是获取一些简单的数据就需要写5-6行代码。

所以，我们引入了“SuperEntity”的概念，通过cglib代理的方式构建了一个代理对象，通过这个代理对象可以直接操作数据库。以相同的场景为例，通过“SuperEntity”我们就可以简化很多代码：

![img](file:///C:\Users\guhong\AppData\Local\Temp\ksohtml25976\wps2.jpg) 

 

使用CrudUtil的getSuperEntity方法，传入对应的IService和主键值（ID）即可获得一个“SuperEntity”实例，这个“SuperEntity”实例直接用我们具体的实体类类型接收。

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps3.jpg) 



当我们使用“SuperEntity”实例调用其getXX或setXX方法时，将会进入名为SuperEntityProxy的类中进行增强处理。这个SuperEntityProxy类实现了Spring框架的MethodInterceptor接口，它代表着一个方法拦截器，而方法拦截器是一种设计模式，它允许在调用目标方法之前、之后或代替目标方法执行特定的逻辑

![img](file:///C:\Users\guhong\AppData\Local\Temp\ksohtml25976\wps4.jpg) 

 

在SuperEntityProxy的拦截中，我们对以get或set为前缀的方法做了拦截和增强，让它们可以直接去操作数据。

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps5.jpg) 

 

需要注意，在使用的过程中，不要将“SuperEntiy”实例作为参数进行传递。以免出现其他开发人员在不知情的情况下进行了数据库的修改，导致数据错乱。



#####  “Tree”模型

在日常业务开发中，我们或许经常会碰到需要查询一个类似树型的数据结构。它们有这相同的特点，即：它是一种非线性的数据结构，由n（n>=0）个有限结点组成一个具有层次关系的集合。每个节点都可能包含一个父节点和若干个子节点。

最常见的例子就是分类功能。分类通常会有2级或3级，甚至更多。就像一颗倒过来的树。

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps6.jpg) 

 

在实际开发中，这种业务代码的查询逻辑几乎是一样的，无非是先获取到父节点，然后递归获得所有子节点。所以，为了简化开发，我们提出了“Tree”模型。

在guhong.play.mybatisplusenhancer.util.tree包中有两个接口，分别是：Node和TreeQueryService。

![img](file:///C:\Users\guhong\AppData\Local\Temp\ksohtml25976\wps7.jpg) 



Node表示一个树形数据结构的节点，它定义了子节点和父节点的命名方式和获取方式。

TreeQueryService表示一个查询服务，用于查询数据并构建树形结构。

 

还是以分类功能为例，假设需要查询所有父级分类以及其下面的所有子分类。使用Node和TreeQueryService接口只需要两步就可以轻松该需求。

首先我们定义一个Node节点：ClassificationNodeVO，让它实现Node接口相应的方法。

![img](file:///C:\Users\guhong\AppData\Local\Temp\ksohtml25976\wps8.jpg) 

 

再定义一个查询服务：SystemClassificationServiceImpl，让它实现TreeQueryService接口的查询方法

![img](file:///C:\Users\guhong\AppData\Local\Temp\ksohtml25976\wps9.jpg) 

 

最后，只需要使用TreeQueryService接口提供的默认方法：buildTree即可自动构建一个树形结构。

![img](file:///C:\Users\guhong\AppData\Local\Temp\ksohtml25976\wps10.jpg) 



##### MetaObjectHandler

在Mybatis Plus中有一个自动填充功能。即，在插入/更新数据时可以根据注解标识为特定的字段自动填充值。详情可以查看Mybatis Plus官网关于自动填充功能的介绍：https://baomidou.com/pages/4c6bcf/

 

因为具体填充什么字段是需要单独编码来决定的，但在实际项目中，需要进行自动填充的字段往往是相似的。例如：创建时间（createTime）、创建人（createUser）、更新时间（updateTime）等。所以，我们对于这些常用的字段提供了对应的自动填充。

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps11.jpg) 

 

需要注意，因为我们项目只是基于Spring AOP对Mybatis Plus的进一步增强，不涉及到任何登录/认证体系。所以在填充创建人（createUser）和更新人（updateUser）时，MybatisPlusMetaObjectHandler需要依赖于UserSupport接口去获取当前用户的id。

![img](https://gitee.com/gitee-guhong/mybatis-plus-enhancer/raw/master/img/wps12.jpg) 

 

所以，在使用MybatisPlusMetaObjectHandler填充创建人（createUser）和更新人（updateUser）时需要开发这用自己项目的登录/认证体系实现UserSupport接口并将实现类注入到Spring中，否则会填充失败。