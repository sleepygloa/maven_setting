package com.seonhoblog.manage.svce;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.common.util.FileUtils;
import com.core.mvc.ParagonService;
import com.core.parameters.Params;
import com.core.parameters.datatable.DataTable;
import com.core.parameters.datatable.datarow.DataRow;

@Service
public class BlogService extends ParagonService{

	private static final Log LOG = LogFactory.getLog(BlogService.class);
	
	@Resource(name="fileUtils")
	private FileUtils fileUtils;
	
	/**
	 * 블로그 그리드 조회
	 * */
	public Params listBlog(Params inParams) throws Exception{
		LOG.debug("listBlog" + inParams);
		return getSqlManager().selectGridParams("BlogService.listBlog", inParams);
	}

	/**
	 * 블로그 글 조회
	 * */
	public Params viewBlog(Params inParams) throws Exception{
		System.out.println("viewBlog" + inParams);
		Params outParams = getSqlManager().selectGridParams("BlogService.viewBlog", inParams);
//		DataTable dt = outParams.getDataTable("dt_data");
		
		//글의 내용이 있을 때.
//		if(dt.size() > 0) {
//			//글 수정 일때.
//			if(inParams.getString("UPDATE") == null) {
//				for(int i = 0; i < dt.size(); i++) {
//					if(dt.get(i).get("TYPE") == null){
//						
//					}else if(!(dt.get(i)).get("TYPE").equals("img")) {
//					
////						String content = (String)((list.get(i)).get("CONTENT"));
////						content = content.replaceAll("<", "&lt");
////						content = content.replaceAll(">", "&gt");
////						System.out.println(content);
////						(list.get(i)).put("CONTENT", content);
//					}
//				}
//			}
//			outParams.setParam("dt_contents", dt);
//			
//			//아이디체크
////			if(inParams.getString("s_userId") != null && (list.get(0)).get("IN_USER_ID") != null) {
////				String s_userId = inParams.getString("s_userId");
////				String inUserId = (String)((list.get(0)).get("IN_USER_ID"));
////				if(s_userId.equals(inUserId)) {
////					System.out.println("ID_CHECK_OK");
////					resultMap.put("S_CHECK_ID", true);
////				}
////			}else {
////				resultMap.put("S_CHECK_ID", false);
////			}
//		}

		//업로드된 파일이있을 때.
//		List<Map<String,Object>> fileList = blogDao.selectFileList(inParams);
//		if(fileList != null) {
//			resultMap.put("dt_file", fileList);
//		}
		System.out.println("d");
		return outParams;
	}
	
	
	public Params saveBlogContents(Params inParams) {
		System.out.println("saveBlogContents" + inParams);
		
		String idx = "";
		
		//글 수정
		if(inParams.getString("idx")!=null) {
			//글 제목 M 저장
			getSqlManager().update("BlogService.saveBlogContentsSubjectUpdate", inParams);
			
			idx = inParams.getString("idx");
		}else {
			//글 제목 M 저장
			getSqlManager().update("BlogService.saveBlogContentsSubjectInsert", inParams);

			//글 제목 M 찾기
			idx = getSqlManager().selectOne("BlogService.listBlogContentsMaxIdx", inParams);
			
			inParams.setParam("idx", idx);
		}
		
		System.out.print("idx "+ idx);
			
		//기존 내용 삭제
		getSqlManager().update("BlogService.saveBlogContentsDelete", inParams);
		
		//신규내용 저장
		DataTable dt = inParams.getDataTable("dt_data");
		for(DataRow dr : dt) {
			//글 제목 M 추가
			dr.setParam("idx", idx);
			
			//글 내용 저장
			getSqlManager().update("BlogService.saveBlogContentsInsert", dr);
		}
		
		return inParams;
	}
	
	public Params listReBlog(Params inParams) {
		System.out.println("listReBlog" + inParams);
		LOG.debug("listReBlog" + inParams);
		return getSqlManager().selectGridParams("BlogService.listReBlog", inParams);
	};
	
	
	public Params saveReBlog(Params inParams, HttpServletRequest request) {
		System.out.println("saveReBlog data : "+ inParams);
		
		
		String modFlag = inParams.getString("flag");
		String reLevel = inParams.getString("reLevel");
		
		if(reLevel.equals("1")) {
			if(modFlag.equals("DELETE")) {
				getSqlManager().update("BlogService.saveReBlogDelete", inParams);
			}else if(modFlag.equals("UPDATE")) {
				getSqlManager().update("BlogService.saveReBlogUpdate", inParams);
			}else {
				getSqlManager().update("BlogService.saveReBlogInsert", inParams);
			}
		}else {
			if(modFlag.equals("DELETE")) {
				getSqlManager().update("BlogService.saveReReBlogDelete", inParams);
			}else if(modFlag.equals("UPDATE")) {
				getSqlManager().update("BlogService.saveReReBlogUpdate", inParams);
			}else {
				getSqlManager().update("BlogService.saveReReBlogInsert", inParams);
			}
		}
		

		
		return inParams;
	}
	
//	@Override
//	public List<Map<String, Object>> getBlogTitleDropdown(Params inParams) throws Exception{
//		return blogDao.getBlogTitleDropdown(inParams);
//	}
//	
//	@Override
//	public void insertBlogAddContent(Params inParams) throws Exception{
//		blogDao.insertBlogAddContent(inParams);
//	}
//	

//	
//	@Override
//	public void deleteBlogContent(Params inParams) throws Exception{
//			
//		blogDao.deleteBlogContentBox(inParams);
//		
//		blogDao.deleteBlogTitleContent(inParams);
//	}
//	
//	
//	@Override
//	public void saveBlogFileUpload(Params inParams, MultipartHttpServletRequest req) throws Exception{
//		
//		blogDao.deleteMainBlogFileList(inParams);
//		List<Map<String,Object>> list = fileUtils.parseUpdateFileInfo(inParams, req);
//		Map<String,Object> tempMap = null;
//		for(int i=0, size=list.size(); i<size; i++){
//			tempMap = list.get(i);
//			if(tempMap.get("IS_NEW").equals("Y")){
//				blogDao.insertMainBlogFile(tempMap);
//			}else{
//				blogDao.updateMainBlogFile(tempMap);
//			}
//		}
//	}
//	

//	
//	@Override
//	public void deleteReBlog(Params inParams) throws Exception {
//		blogDao.deleteReBlog(inParams);
//	}
//	
//	@Override
//	public void deleteReBlogRefAll(Params inParams) throws Exception {
//		blogDao.deleteReBlogRefAll(inParams);
//	}
//	
//	@Override
//	public void saveReReBlog(Params inParams, HttpServletRequest request) throws Exception {
//		System.out.println("saveReReBlog data : "+ inParams);
//		blogDao.saveReReBlog(inParams);
//	}
	
}
