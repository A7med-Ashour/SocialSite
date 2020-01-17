package help;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;


public class Helper {
	
	public static <T> void  validateFieldsWithPatterns(T obj,Map<String,String> patterns,Map<String,Boolean> validationResults){
			
			
			patterns.forEach((name,pattern) -> {
				try {
					Field f = obj.getClass().getDeclaredField(name);
					f.setAccessible(true);
					boolean isValid = Pattern.matches(pattern, f.get(obj).toString());
					validationResults.put(name, isValid);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			});
			
	}
	
	public static <T> void  validateFieldsWithPatterns(T obj,Map<String,String> patterns,
													         Map<String,String> successMessages,
													         Map<String,String> failMessages,
													         Map<String,String> resultMessages){
		
		Map<String,Boolean> booleanResult = new HashMap<>();
		validateFieldsWithPatterns(obj,patterns,booleanResult);
		booleanResult.forEach((name,result) -> {
			if(result) {
				resultMessages.put(name, successMessages.get(name));
			}else {
				resultMessages.put(name, failMessages.get(name));
			}
		});
		
}

	public static <T> void validateFieldsWithPatterns(T obj, Map<String, String> patterns,
			Map<String, Boolean> validationResults, Container<Integer> successCount,  Container<Integer> failCount) {
		
		validateFieldsWithPatterns(obj,patterns,validationResults);
		successCount.setValue((int) validationResults.entrySet().stream().filter(e -> e.getValue()).count());
		failCount.setValue(validationResults.size() - successCount.getValue().intValue());
		
	}
	
	public static <T> void  validateFieldsWithPatterns(T obj,Map<String,String> patterns,
	         Map<String,String> successMessages,
	         Map<String,String> failMessages,
	         Map<String,String> resultMessages,
	         Container<Integer> successCount, Container<Integer> failCount) {
		
		
		validateFieldsWithPatterns(obj,patterns,successMessages,failMessages,resultMessages);
		
		
		successCount.setValue((int) resultMessages.entrySet().stream()
												.filter(e -> successMessages.containsValue(e.getValue()))
												.count());
		
		failCount.setValue(resultMessages.size() - successCount.getValue().intValue());
		
	
	}


	public static String generateRandomString(int length) {
		
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder(length);
		Random r = new Random();
		
		for(int i = 0; i < length ; i++) {
			
			sb.append(chars.charAt(r.nextInt(36)));
		}
		
		return sb.toString();
	}

	
	
}
