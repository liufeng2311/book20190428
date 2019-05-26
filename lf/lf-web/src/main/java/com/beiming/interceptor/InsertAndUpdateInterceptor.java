package com.beiming.interceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.springframework.stereotype.Component;
import com.beiming.entity.User;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * mybatis拦截器，对公共字段（创建人、创建时间、修改人、修改时间、status）赋值
 * 
 * @author zhiguang
 *
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})  //method = "update"表示拦截insert、update和delete操作
@Component
public class InsertAndUpdateInterceptor implements Interceptor {

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    SqlCommandType sqlCommandType = SqlCommandType.UNKNOWN;
    Object[] args = invocation.getArgs();
    MappedStatement mappedStatement = null; 
    int mappedStatementIndex = 0; 
    Object parameterObject = null; 
    for (int i = 0; i < args.length; i++) {
      Object arg = args[i];
      if (arg instanceof MappedStatement) {
        mappedStatementIndex = i;
        mappedStatement = (MappedStatement) arg;
        sqlCommandType = mappedStatement.getSqlCommandType();
      } else {
        parameterObject = args[i];
      }
    }
    if (sqlCommandType == SqlCommandType.UPDATE) {
      args[mappedStatementIndex] = this.updateSql(sqlCommandType, mappedStatement, parameterObject);
      //通用Mapper方法
      //updateByExample()                         属于Map,属于动态SQL
      //updateByExampleSelective()                属于Map,属于动态SQL
      //updateByPrimaryKey()                      不属于Map,属于动态SQL
      //updateByPrimaryKeySelective()             不属于Map,属于动态SQL
      //Mapper接口自定义方法
      //getUser1(@Param("id") String id, @Param("username") String username);          属于map，不属于动态sql
      //getUser1(String id);                                                           不属于map，不属于动态sql
      //getUser1(@Param("id") String id)                                               属于map
      if (parameterObject != null && parameterObject instanceof Map) {
        // 如果是map，有两种情况：（1）使用@Param多参数传入，由Mybatis包装成map。（2）原始传入Map
        setPropertyForMap(mappedStatement, sqlCommandType, parameterObject);
      } else { // 原始参数传入
        setProperty(mappedStatement, sqlCommandType, parameterObject);
      }
    }
    return invocation.proceed();
  }
  
  /**
   * 更新sql，针对mapper.xml里的<update>..........</update>和mapper公共的更新方法updateByPrimaryKey、updateByPrimaryKeySelective方法
   * 
   * @param sqlCommandType
   * @param mappedStatement
   * @param baseObject
   * @return
   */
  private MappedStatement updateSql(SqlCommandType sqlCommandType, MappedStatement mappedStatement, Object baseObject) {
    if (sqlCommandType != SqlCommandType.UPDATE) {
      return mappedStatement;
    }
    try {
      SqlSource sqlSource = mappedStatement.getSqlSource();
      if (sqlSource instanceof DynamicSqlSource && !(baseObject instanceof Map)) { //非map参数动态下添加乐观锁
        DynamicSqlSource dynamicSqlSource = (DynamicSqlSource) sqlSource;
        BoundSql boundSql = dynamicSqlSource.getBoundSql(baseObject);
        String updateSql = boundSql.getSql();
        Integer version = getVersionValue(baseObject);
        if (updateSql.contains(" WHERE  id =") && version != null) {
          updateSql = updateSql.replace(" WHERE  id =", " WHERE version = " + version + " and id =");
          // 把新的查询放到statement里
          MappedStatement newMappedStatement = copyFromMappedStatement(mappedStatement, boundSql, updateSql);
          return newMappedStatement;
        }
      }
    } catch (Exception e) {
      log.error("InsertAndUpdateInterceptor updateSql error {}", e);
    }
    return mappedStatement;
  }

  /**
   * copy新的一个MappedStatement
   * 
   * @param ms
   * @param boundSql
   * @param sql
   * @return
   */
  private MappedStatement copyFromMappedStatement(MappedStatement ms, BoundSql boundSql, String sql) {
    BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
    SqlSource newSqlSource = new SqlSource() {
      @Override
      public BoundSql getBoundSql(Object parameterObject) {
        return newBoundSql;
      }
    };
    
    MappedStatement.Builder builder =
        new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
    builder.resource(ms.getResource());
    builder.fetchSize(ms.getFetchSize());
    builder.statementType(ms.getStatementType());
    builder.keyGenerator(ms.getKeyGenerator());
    if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
      builder.keyProperty(ms.getKeyProperties()[0]);
    }
    builder.timeout(ms.getTimeout());
    builder.parameterMap(ms.getParameterMap());
    builder.resultMaps(ms.getResultMaps());
    builder.resultSetType(ms.getResultSetType());
    builder.cache(ms.getCache());
    builder.flushCacheRequired(ms.isFlushCacheRequired());
    builder.useCache(ms.isUseCache());
    return builder.build();
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties obj) {

  }

  @SuppressWarnings("rawtypes")
  private void setPropertyForMap(MappedStatement mappedStatement, SqlCommandType sqlCommandType, Object arg) {
    if (arg == null) {
      return;
    }
    Map map = (Map) arg;
    Integer version = null; //设置版本号条件
    for (Object key : map.keySet()) {
      Object value = map.get(key);
      //公共mapper的updateByExample、updateByExampleSelective方法，参数包含record和example
      if ("record".equals(key)) { //获取原来的版本号
        try {
          version = this.getVersionValue(value);
        } catch (Exception e) {
          log.error("InsertAndUpdateInterceptor setPropertyForMap error {}", e);
        }
      } else if ("example".equals(key) && value instanceof Example) { //针对公共mapper的updateByExample、updateByExampleSelective方法对更新操作设置版本号条件
        Example example = (Example) value;
        List<Criteria> criterias = example.getOredCriteria();
        if (version != null) {
          example.and(example.createCriteria().andEqualTo("version", version));
        }
        continue;
      } else if ((String.valueOf(key)).contains("param")) {
        continue;
      }
      if (value instanceof List) {
        List list = (List) value;
        for (Object model : list) {
          setProperty(mappedStatement, sqlCommandType, model);
        }
      } else {
        setProperty(mappedStatement, sqlCommandType, value);
      }
    }
  }

  /**
   * 为对象的操作属性赋值
   * @param sqlCommandType
   * @param model
   */
  private void setProperty(MappedStatement mappedStatement, SqlCommandType sqlCommandType, Object model) {
    if (model == null) {
      return;
    }
    SqlSource sqlSource = mappedStatement.getSqlSource();
    if (sqlSource instanceof RawSqlSource) { // 如果是写在mapper的更新语句<update>.......</update>，不对实体设值
      return;
    }
    Field[] fields = model.getClass().getSuperclass().getDeclaredFields();
    if (fields == null || fields.length == 0) { //如果没有继承baseObject
      fields = model.getClass().getDeclaredFields();
    }
    try {
      for (int i = 0; i < fields.length; i++) { // 遍历所有属性
        String fieldName = fields[i].getName(); // 获取属性的名字
        fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method m = model.getClass().getMethod("get" + fieldName);
        if (sqlCommandType == SqlCommandType.INSERT) {
          if ("createTime".equalsIgnoreCase(fieldName)) {
            m = model.getClass().getMethod("set" + fieldName, Date.class);
            m.invoke(model, new Date());
          }
          if ("status".equalsIgnoreCase(fieldName)) {
            m = model.getClass().getMethod("get" + fieldName);
            Integer status = (Integer) m.invoke(model);
            // 对status赋值
            m = model.getClass().getMethod("set" + fieldName, String.class);
            m.invoke(model, null == status ? StatusEnums.NORMAL.getValue() : status);
          }
          if ("version".equalsIgnoreCase(fieldName)) {
            m = model.getClass().getMethod("set" + fieldName, Integer.class);
            m.invoke(model, StormConstant.DEFAULT_VERSION);
          }
        } else if (sqlCommandType == SqlCommandType.UPDATE) {
          if ("updateTime".equalsIgnoreCase(fieldName)) {
            m = model.getClass().getMethod("set" + fieldName, Date.class);
            m.invoke(model, new Date());
          }
          if ("version".equalsIgnoreCase(fieldName)) {
            m = model.getClass().getMethod("get" + fieldName);
            Integer version = (Integer) m.invoke(model);
            m = model.getClass().getMethod("set" + fieldName, Integer.class);
            m.invoke(model, version == null ? StormConstant.DEFAULT_VERSION : version + 1);
          }
        }
      }
    } catch (Exception e) {
      log.error("InsertAndUpdateInterceptor setProperty error {}", e);
    }
  }
  
  /**
   * 通过baseObject获取version的值
   * @param baseObject
   * @return
   */
  private Integer getVersionValue(Object baseObject) {
    Integer version = null;
    if (!(baseObject instanceof Map)) {
      try {
        Field field = null;
        if (User.class.getName().equals(baseObject.getClass().getName())) {
          field = baseObject.getClass().getDeclaredField("version");
        } else {
          field = baseObject.getClass().getSuperclass().getDeclaredField("version");
        }
        if (field == null) {
          field = baseObject.getClass().getDeclaredField("version");
        }
        if (field == null) {
          return version;
        }
        String fieldName = field.getName();
        fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method m = baseObject.getClass().getMethod("get" + fieldName);
        version = (Integer) m.invoke(baseObject);
      } catch (Exception e) {
        log.error("InsertAndUpdateInterceptor getVersionValue error {}", e);
      }
    }
    return version;
  }
  
}
