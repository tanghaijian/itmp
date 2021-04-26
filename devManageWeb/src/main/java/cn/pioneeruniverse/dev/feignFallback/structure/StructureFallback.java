package cn.pioneeruniverse.dev.feignFallback.structure;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.pioneeruniverse.dev.feignInterface.structure.StructureInterface;

import feign.hystrix.FallbackFactory;
@Component
public class StructureFallback implements FallbackFactory<StructureInterface> {

	@Override
	public StructureInterface create(Throwable cause) {
		
		return new StructureInterface(){

			@Override
			public Map<String, Object> getAllSystemInfo(Integer pageIndex, Integer pageSize) {
				Map<String , Object> map=new HashMap<>();
				map.put("ds", "gggg");
				return map;
			}

			@Override
			public Map<String, Object> getSystemModule(Integer id) {
				Map<String , Object> map=new HashMap<>();
				map.put("ds", "fff");
				return map;
			}

			@Override
			public Map<String, Object> creatJenkinsJob(Integer systemId, String systemName, String serverType,
					String[] module) {
				// TODO Auto-generated method stub
				return null;
			}

			
			
			
			
			
			


		
			
		};
	}

}
