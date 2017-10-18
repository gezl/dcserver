package com.dc.server.key;

public class TypeEnum {
	
	
	
	public enum DBType {
		oracle(1, "oracle"), mysql(2, "mysql"),sqlserver(3,"sqlserver");

		private final int key;

		private final String name;

		private DBType(int key, String name) {

			this.key = key;
			this.name = name;
		}

		public int getKey() {

			return key;
		}

		public String getName() {

			return name;
		}

		public static String getValue(int key) {

			DBType dbType[] = DBType.values();
			for (DBType type : dbType) {
				if (type.getKey() ==key) {
					return type.getName();
				}
			}
			return "";
		}
	}

	public enum LineType {
		ON("On-line", "在线"), OFF("Off-line", "离线");

		private final String key;

		private final String name;

		private LineType(String key, String name) {

			this.key = key;
			this.name = name;
		}

		public String getKey() {

			return key;
		}

		public String getName() {

			return name;
		}

		public static String getValue(String key) {

			LineType lineType[] = LineType.values();
			for (LineType type : lineType) {
				if (type.getKey().equals(key)) {
					return type.getName();
				}
			}
			return "";
		}
	}

	public enum EnabledType {
		ENABLE(1, "启用"), DISABLED(2, "禁用");

		private final int key;

		private final String name;

		private EnabledType(int key, String name) {

			this.key = key;
			this.name = name;
		}

		public int getKey() {

			return key;
		}

		public String getName() {

			return name;
		}

		public static String getValue(int key) {

			EnabledType enabledType[] = EnabledType.values();
			for (EnabledType type : enabledType) {
				if (type.getKey() == key) {
					return type.getName();
				}
			}
			return "";
		}
	}

	public enum UserType {
		ADMIN("admin", "管理员"), USER("user", "客户端");

		private final String key;

		private final String name;

		private UserType(String key, String name) {

			this.key = key;
			this.name = name;
		}

		public String getKey() {

			return key;
		}

		public String getName() {

			return name;
		}

		public static String getValue(String key) {

			UserType userType[] = UserType.values();
			for (UserType type : userType) {
				if (type.getKey().equals(key)) {
					return type.getName();
				}
			}
			return "";
		}
	}
	
	
	public enum RowType {
		OLD(1, "原始数据"), NEW(2, "新增"),EXECUTE(3,"执行");
		
		private final int key;

		private final String name;

		private RowType(int key, String name) {

			this.key = key;
			this.name = name;
		}

		public int getKey() {

			return key;
		}

		public String getName() {

			return name;
		}

		public static String getValue(int key) {

			RowType rowType[] = RowType.values();
			for (RowType type : rowType) {
				if (type.getKey() == key) {
					return type.getName();
				}
			}
			return "";
		}
	}
	
	
	public enum SqlStateType {
		NOTEXECUTE("1", "未执行"),EXECUTE("2","执行");
		
		private final String key;

		private final String name;

		private SqlStateType(String key, String name) {

			this.key = key;
			this.name = name;
		}

		public String getKey() {

			return key;
		}

		public String getName() {

			return name;
		}

		public static String getValue(String key) {

			SqlStateType sqlStateType[] = SqlStateType.values();
			for (SqlStateType type : sqlStateType) {
				if (type.getKey().equals(key)) {
					return type.getName();
				}
			}
			return "";
		}
	}
	
	
	public enum ErrorType {
		DATA_EX(1, "数据异常"), SOCKET_EX(2, "通讯异常"),SOCKET_EX_TIME_OUT(3, "通讯超时异常");

		private final int key;

		private final String name;

		private ErrorType(int key, String name) {

			this.key = key;
			this.name = name;
		}

		public int getKey() {

			return key;
		}

		public String getName() {

			return name;
		}

		public static String getValue(int key) {

			ErrorType errorType[] = ErrorType.values();
			for (ErrorType type : errorType) {
				if (type.getKey() == key) {
					return type.getName();
				}
			}
			return "";
		}
	}
	
	
	
}
