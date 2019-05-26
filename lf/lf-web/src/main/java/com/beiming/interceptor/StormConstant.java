package com.beiming.interceptor;

/**
 * @author lb
 */
public class StormConstant {

  public static final String URL_PREFIX = "/storm";

  /**
   * 数据库里默认的version值
   */
  public static final Integer DEFAULT_VERSION = 0;

  /**
   * 正则：手机号（精确）
   * <p>
   * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
   * </p>
   * <p>
   * 联通：130、131、132、145、155、156、175、176、185、186、166
   * </p>
   * <p>
   * 电信：133、153、173、177、180、181、189、199
   * </p>
   * <p>
   * 全球星：1349
   * </p>
   * <p>
   * 虚拟运营商：170
   * </p>
   */
  public static final String REGEX_MOBILE_EXACT =
      "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
  /**
   * 正则： 身份证格式是否正确
   */
  public static final String REGEX_ID_CARD =
      "^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$";
  /**
   * 正则表达式: 验证密码的格式
   */
  public static final String REGEX_PASSWORD = "^[0-9a-zA-Z]{8,16}$";

  /**
   * 正则表达式：验证短信验证码格式
   */
  public static final String REGEX_VALIDATE_CODE = "^[0-9]{4}$";
  /**
   * 默认密码
   */
  public static final String COMMON_PASSWORD = "88888888";

  /**
   * 禁用状态
   */
  public static final String DISABLE_STATUS_VALUE = "1";

  /**
   * 邮箱的格式
   */
  public static final String REGEX_EMAIL =
      "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

  /**
   * 查询条件为手机号码
   */
  public static final String SEARCH_MOBILE_PHONE = "mobilePhone";

  /**
   * 查询条件为登录名
   */
  public static final String SEARCH_LOGIN_NAME = "loginName";

  /**
   * 默认每页大小
   */
  public static final Integer DEFAULT_PAGE = 10;


  /**
   * 预览文件url前缀
   */
  public static final String PREVIEW_URL_PREFIX = URL_PREFIX + "/file/previewImg/";
  /**
   * 文件下载url前缀
   */
  public static final String DOWNLOAD_URL_PREFIX = URL_PREFIX + "/file/download/";


  /**
   * 是
   */
  public static final Integer YES = 1;
  /**
   * 否
   */
  public static final Integer NO = 0;

  /**
   * 默认计数0
   */
  public static final Integer DEFAULT_COUNT = 0;

  /**
   * 发帖实名要求
   */
  public static final String CODE_POSTS_REAL_NAME = "POSTS_REAL_NAME";

  /**
   * 字典缓存时间
   */
  public static final Long DICTIONARY_REDIS_ALIVE_TIME = 30L;

  public static final String DOCUMENT_TYPE = "meeting";

}
