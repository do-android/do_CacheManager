package doext.implement;

import org.json.JSONObject;

import core.helper.cache.DoCacheManager;
import core.interfaces.DoIScriptEngine;
import core.object.DoInvokeResult;
import core.object.DoSingletonModule;
import doext.define.do_CacheManager_IMethod;

/**
 * 自定义扩展SM组件Model实现，继承DoSingletonModule抽象类，并实现do_CacheManager_IMethod接口方法；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象；
 * 获取DoInvokeResult对象方式new DoInvokeResult(this.getUniqueKey());
 */
public class do_CacheManager_Model extends DoSingletonModule implements do_CacheManager_IMethod{

	public do_CacheManager_Model() throws Exception {
		super();
	}
	
	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, JSONObject _dictParas,
			DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult)
			throws Exception {
		//...do something
		return super.invokeSyncMethod(_methodName, _dictParas, _scriptEngine, _invokeResult);
	}
	
	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用，
	 * 可以根据_methodName调用相应的接口实现方法；
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名
	 * #如何执行异步方法回调？可以通过如下方法：
	 * _scriptEngine.callback(_callbackFuncName, _invokeResult);
	 * 参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 * 获取DoInvokeResult对象方式new DoInvokeResult(this.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, JSONObject _dictParas,
			DoIScriptEngine _scriptEngine, String _callbackFuncName)throws Exception {
		if ("getImageCacheSize".equals(_methodName)) {
			getImageCacheSize(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
	    }
	    if ("clearImageCache".equals(_methodName)) {
	    	clearImageCache(_dictParas, _scriptEngine, _callbackFuncName);
	    	return true;
	    }
		return super.invokeAsyncMethod(_methodName, _dictParas, _scriptEngine, _callbackFuncName);
	}


	/**
	 * 清除图片缓存；
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_callbackFuncName 回调函数名
	 */
	@Override
	public void clearImageCache(JSONObject _dictParas, DoIScriptEngine _scriptEngine,String _callbackFuncName) {
		DoInvokeResult _invokeResult = new DoInvokeResult(getUniqueKey());
		boolean result = DoCacheManager.getInstance().deleteDiskCache();
		_invokeResult.setResultBoolean(result);
		_scriptEngine.callback(_callbackFuncName, _invokeResult);
	}

	/**
	 * 获取图片缓存；
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_callbackFuncName 回调函数名
	 */
	@Override
	public void getImageCacheSize(JSONObject _dictParas, DoIScriptEngine _scriptEngine,String _callbackFuncName) {
		DoInvokeResult _invokeResult = new DoInvokeResult(getUniqueKey());
		int currImageCacheSize = DoCacheManager.getInstance().getCacheSize();
		_invokeResult.setResultText(String.valueOf(currImageCacheSize / 1024));
		_scriptEngine.callback(_callbackFuncName, _invokeResult);
	}
}