package com.dc.platform.permission;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


public class PermissionUtil{
	
	/**
	 * 查看 1
	 */
	public static final int READ = 1;
	/**
	 * 添加 2
	 */
	public static final int CREATE = 2;
	/**
	 * 修改 3
	 */
	public static final int UPDATE = 3;
	/**
	 * 删除 4
	 */
	public static final int DELETE = 4;
	/**
	 * 审核 5
	 */
	public static final int AUDIT = 5;
	/**
	 * 查询配置 6
	 */
	public static final int SEARCHCONFIG = 6;
	/**
	 * 导出 7
	 */
	public static final int EXPORT = 7;
	/**
	 * 授权 8
	 */
	public static final int ACCREDIT = 8;
	
	/**
	 * 物资拆分 9
	 */
	public static final int SPLIT = 9;
	
	/**
	 * 询价组合 10
	 * 
	 */
	public static final int COMBINATION = 10; 
	/**
	 * 加入计划
	 */
	public static final int JOINPLAN=11;
	/**
	 * 确认
	 */
	public static final int  ARRIRM=12;
	/**
	 * 允许撤回
	 */
	public static final int  ALLOWBACK=13;
	/**
	 * 保存修改
	 */
	public static final int  SAVEUPDATE=14;
	/**
	 * 移垛完成
	 */
	public static final int  MOVESTOCKFINISH=15;
	/**
	 * 合同总量修改
	 */
	public static final int  UPDATEQUANTITY=16;
	/**
	 * 合同拆分
	 */
	public static final int  PACTSPLIT=17;
	/**
	 * 加入当班
	 */
	public static final int  JOINWORK=18;
    /**
     * 暂停
     */
	public static final int  WORKSTOP=19;
	/**
	 * 继续
	 */
	public static final int  WORKCONTINUE=20;
	/**
	 * 完成
	 */
	public static final int  FINISH=21;
	/**
	 * 启用
	 */
	public static final int STARTPLAN=22;
	/**
	 * 调垛
	 */
	public static final int ADJUSTSTOCK=11;
	/**
	 * 移垛
	 */
	public static final int MOVESTOCK=12;
	
	/**
	 * 货物垛位管理
	 */
	public static final int GOODSMANAGER=13;
	/**
	 * 申请退回
	 */
	public static final int APPLYBACK=12;
	
	/**
	 * 下发通知
	 */
	public static final int ISSUEDINFORM=11;
	/**
	 * 结船
	 */
	public static final int ISFILEBOAT=11;
	/**
	 * 申请装车
	 */
	public static final int APPLYTRUNK=11;
	/**
	 * 合同延期
	 */
	public static final int PACTDELAY=11;
	/**
	 * 查看合并通知单
	 */
	public static final int SEEINFORM=12;
	/**
	 * 交班
	 */
	public static final int GIVE=11;
	/**
	 * 接班
	 */
	public static final int ACCEPT=12;
	/**
	 * 详情
	 */
	public static final int SEARCHINFO =13;
	/**
	 * 打印榜单
	 */
	public static final int PRINTBANG=14;
	/**
	 * 修改过磅记录
	 */
	public static final int UPDATECARWEIGHTRECORDS=11;
	
/*费用*/
	
	/**
	 * 客户账目查询——退款
	 */
	public static final int SO_FEE_BACK=11;
	/**
	 * 客户账目查询——修改余额
	 */
	public static final int UPDATE_FEE_AMOUNT=12;
	/**
	 * 港使费率维护---启用
	 */
	public static final int RATE_ACTIVE=11;
	/**
	 * 港使费率维护---禁用
	 */
	public static final int RATE_UNABLE=12;
	/**
	 * 港使，拖轮费计算-- 计算
	 */
	public static final int BOATUSEFEE_JS=11;
	/**
	 * 港使，拖轮费计算-- 付款方式
	 */
	public static final int BOATUSEFEE_FK=12;
	/**
	 * 速遣费计算-- 计算
	 */
	public static final int LAGFEE_JS=11;
	/**
	 * 速遣计算-- 付款方式
	 */
	public static final int LAGFEE_FK=12;
	/**
	 * 装卸费计算-- 计算
	 */
	public static final int LOADFEE_JS=11;
	/**
	 * 装卸费计算-- 付款方式
	 */
	public static final int LOADFEE_FK=12;
	/**
	 * 堆存费计算-- 计算
	 */
	public static final int STACKFEE_JS=11;
	/**
	 * 堆存费计算-- 付款方式
	 */
	public static final int STACKFEE_FK=12;
	
	/**
	 * 其他费用-- 确认计算计算
	 */
	public static final int OTHERFEE_JS=11;
	/**
	 * 其他费用-- 付款方式
	 */
	public static final int OTHERFEE_FK=12;
	
	/**
	 * 应收款信息- 收款
	 */
	public static final int COLLECTIONFEE=11;
	/**
	 * 应收款信息- 退回重计算
	 */
	public static final int BACKFEE_JS=12;
	/**
	 * 月份费收台账--统计
	 */
	public static final int monthaccount_tj=11;
	/**
	 * 月份欠款台账--统计
	 */
	public static final int montharrearage_tj=11;
/*堆场*/
	/**
	 * 新增移垛通知
	 */
	public static final int ADDMOVEPILE=11;
	/**
	 * 垛位全移确认
	 */
	public static final int ALLMOVE=13;
	/**
	 * 删除移垛通知
	 */
	public static final int DELETEMOTENOTE=14;
	/**
	 * 垛位恢复
	 */
	public static final int RESTORE=11;
	
	/**
	 * 货种变更
	 */
	public static final int UPDATEMINE=14;
	/**
	 * 
	 * @param SN
	 * @param req
	 * @return
	 */

	public static boolean hasPermission(String SN,HttpServletRequest req){
		Map<String,Integer> MODULE_SESSION=PermissionUtil.getSessionModuleSNMap(req);
		if(MODULE_SESSION.get(SN)!=null&&MODULE_SESSION.get(SN)>0)
			return true;
		else
			return false;
	}
	
	public static Set<String> getSessionModuleSNSet(HttpServletRequest req){
		Map<String,Integer> MODULE_SESSION = PermissionUtil.getSessionModuleSNMap(req);
		Set<String> snSet= MODULE_SESSION.keySet();
		return snSet;
	}
	public static Map<String,Integer> getSessionModuleSNMap(HttpServletRequest req){
		Map<String,Integer> MODULE_SESSION = (Map<String,Integer>)req.getSession().getAttribute("MODULE");
		return MODULE_SESSION;
	}
	
	/**
	 * 将模块的操作权限转成视图形式。
	 * @param permission
	 * @param aclstate
	 * @return
	 */
	public static boolean checkPermission(int permission,int aclstate){
		
		int temp = 1;
		temp = temp << permission;
		temp = aclstate & temp;
		if(temp != 0){
			return true;
		}
		return false;
	}
	
	public static int setPermission(int permission,int aclstate){
		int temp = 1;
		temp = temp << permission;
		aclstate = aclstate | temp;
		return aclstate;
	}
	/**
	 * 合并权限值
	 * @param control
	 * @param o_control
	 * @return
	 */
	public static int mergePermission(int control,int o_control){
		return control | o_control;
	}
	
	/**
	 * 通过用户角色和模块sn得到相应的权值
	 * @param permissionUser(用户角色｛user,role｝)
	 * @param sn(模块sn)
	 * @return 模块权值
	
	public static boolean moduleControl(User user,String sn,int aclstate){
		if(user.getName().endsWith("dc_admin")){
			return true;
		}else{
			Map<String,Object> ModuleContainer = user.getModuleContainer();
			
			if(ModuleContainer.get("role")!=null){
				List<SysRolePermission> roleList = (List<SysRolePermission>)ModuleContainer.get("role");
				for (SysRolePermission sysRolePermission : roleList) {
					if(sysRolePermission.getModule_sn().equals(sn)){
						return checkPermission(aclstate,sysRolePermission.getContorl());
					}
				} 
			}
			if(ModuleContainer.get("user")!=null){
				Map<String,Integer> userMap = (Map<String,Integer>)ModuleContainer.get("user");
				for (Iterator iterator = userMap.keySet().iterator(); iterator.hasNext();) {
					String key = (String) iterator.next();
					if(userMap.containsKey(sn)){
						return checkPermission(aclstate,userMap.get(sn));
					}
				}
			}
		}
		return false;
	}
 */
}
