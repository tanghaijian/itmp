package cn.pioneeruniverse.common.subversion;

import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.dev.entity.ChangedPath;
import cn.pioneeruniverse.dev.entity.SvnCommitLog;
import cn.pioneeruniverse.dev.entity.SvnFileDirectoryStructure;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 
* @ClassName: SubversionUtils
* @Description: SVN工具类
* @author author
* @date 2020年8月17日 下午8:03:32
*
 */
public class SubversionUtils {

    private static Logger logger = LoggerFactory.getLogger(SubversionUtils.class);

    private static SubversionWbsHelper subversionWbsHelper = SpringContextHolder.getBean(SubversionWbsHelper.class);

    /**
     * @return void
     * @Description 根据不同协议，初始化不同的仓库工厂。
     * @MethodName setUpFactory
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/10/30 13:37
     */
    private static void setUpFactory() {
        //svn://, svn+xxx:// (svn+ssh:// in particular)
        SVNRepositoryFactoryImpl.setup();
        //http:// and https://
        DAVRepositoryFactory.setup();
        //file:///
        FSRepositoryFactory.setup();
    }

    /**
     * @param url      仓库根目录
     * @param username svn账户
     * @param password svn密码
     * @return org.tmatesoft.svn.core.io.SVNRepository
     * @Description 创建仓库驱动
     * @MethodName createRepositoryDriver
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/10/30 13:39
     */
    public static SVNRepository createRepositoryDriver(String url, String username, String password) {
        setUpFactory();
        SVNRepository repository = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
            //身份验证
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
            repository.setAuthenticationManager(authManager);
            repository.testConnection();
            return repository;
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return null;
        }
    }

    /**
     * @param username 仓库账户
     * @param password 仓库密码
     * @return org.tmatesoft.svn.core.wc.SVNClientManager
     * @Description 初始化svn客户端管理类
     * @MethodName setUpSVNClient
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/10/30 13:40
     */
    public static SVNClientManager setUpSVNClient(String username, String password) {
        setUpFactory();
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        return SVNClientManager.newInstance((DefaultSVNOptions) options, username, password);
    }

    /**
     * @param clientManager
     * @param url
     * @param commitMessage 提交注释
     * @return org.tmatesoft.svn.core.SVNCommitInfo
     * @Description 仓库中新增文件夹
     * @MethodName makeDirectory
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/10/30 13:41
     */
    public static SVNCommitInfo makeDirectory(SVNClientManager clientManager, SVNURL url, String commitMessage) {
        try {
            return clientManager.getCommitClient().doMkDir(
                    new SVNURL[]{url}, commitMessage);
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return null;
        }
    }

    /**
     * @param clientManager
     * @param wcPath    本地svn的工作副本路径
     * @param remote        true:检查存储库中的项的状态，确定本地项是否过期。
     * @return org.tmatesoft.svn.core.wc.SVNStatus
     * @Description 获取本地工作副本状态信息
     * @MethodName getStatus
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/10/30 13:43
     */
    public static SVNStatus getStatus(SVNClientManager clientManager, File wcPath, boolean remote) {
        SVNStatus status = null;
        if (isWorkingCopy(wcPath)) {
            try {
                status = clientManager.getStatusClient().doStatus(wcPath, remote);
            } catch (SVNException e) {
                logger.error(String.valueOf(e.getErrorMessage()), e);
                return null;
            }
        }
        return status;
    }

    /**
     * @param local true是，false不是
     * @return boolean
     * @Description 判断本地文件夹是否为工作副本
     * @MethodName isWorkingCopy
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/10/30 13:43
     */
    public static boolean isWorkingCopy(File local) {
        if (!local.exists()) {
            logger.warn(local.getAbsolutePath() + "is not exist!");
            return false;
        } else {
            try {
                if (SVNWCUtil.getWorkingCopyRoot(local, false) == null) {
                    return false;
                }
            } catch (SVNException e) {
                logger.error(String.valueOf(e.getErrorMessage()), e);
                return false;
            }
        }
        return true;
    }

    /**
     * @param repository
     * @param dirPath        被查询目录路径
     * @param fileNameSearch 文件名过滤
     * @return java.util.List<org.tmatesoft.svn.core.SVNDirEntry>
     * @Description
     * @MethodName getSvnDirEntry
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/10/30 13:45
     */
    public static List<SVNDirEntry> getSvnDirEntry(SVNRepository repository, String dirPath, String fileNameSearch) {
        List<SVNDirEntry> result = new LinkedList<SVNDirEntry>();
        //修订版本号，-1代表一个无效的修订版本号，代表必须是最新的修订版
        long revisionNum = -1;
        try {
            /**
             * SVNNodeKind.NONE    这个node已经丢失（可能是已被删除）
             * SVNNodeKind.FILE    文件
             * SVNNodeKind.DIR     目录
             * SVNNodeKind.UNKNOW  未知，无法解析
             */
            SVNNodeKind svnNodeKind = repository.checkPath(dirPath, revisionNum);
            //必须为目录
            if (svnNodeKind != SVNNodeKind.DIR) {
                return null;
            }
            listSvnDirEntry(result, repository, dirPath, fileNameSearch);
            return result;
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return null;
        }
    }


    /**
     * @param repositoryUrl svn仓库url
     * @param username 账户
     * @param password 密码
     * @param ip 
     * @param port
     * @param dirPath 指定的目录
     * @param repositoryName 仓库名
     * @param fileNameSearch 指定的文件名
     * @return cn.pioneeruniverse.dev.entity.SvnFileDirectoryStructure
     * @Description 获取svnUrl下文件目录（树状格式）
     * @MethodName getSvnDirEntry
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/13 11:27
     */
    public static SvnFileDirectoryStructure getSvnDirEntry(String repositoryUrl, String username, String password, Integer accessProtocol, String ip, String port, String dirPath, String repositoryName, String fileNameSearch) {
        //修订版本号，-1代表一个无效的修订版本号，代表必须是最新的修订版
        long revisionNum = -1;
        try {
            SVNRepository svnRepository = createRepositoryDriver(repositoryUrl, username, password);
            if (svnRepository == null) {
                return null;
            }
            SVNNodeKind svnNodeKind = svnRepository.checkPath(dirPath, revisionNum);
            String svnUrl = repositoryUrl + dirPath;
            SvnFileDirectoryStructure structure = new SvnFileDirectoryStructure(repositoryName, SVNNodeKind.DIR.getID(), repositoryName, dirPath, svnUrl, accessProtocol, ip, port, repositoryUrl, username, password);
            //必须为目录
            if (svnNodeKind != SVNNodeKind.DIR) {
                return null;
            }
            listEntry(structure, svnRepository, dirPath, fileNameSearch, repositoryUrl, accessProtocol, username, password);
            return structure;
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return null;
        }
    }


    /**
     * 
    * @Title: listEntry
    * @Description: svn路径下的文件清单
    * @author author
    * @param structure svn结构
    * @param repository 仓库
    * @param filePath 文件路径
    * @param fileNameSearch 查找的文件名
    * @param repositoryUrl svn仓库url
    * @param accessProtocol
    * @param username
    * @param password
    * @throws SVNException
     */
    private static void listEntry(SvnFileDirectoryStructure structure, SVNRepository repository, String filePath, String fileNameSearch, String repositoryUrl, Integer accessProtocol, String username, String password) throws SVNException {
        Collection entry = repository.getDir(filePath, -1, null, (Collection) null);
        if (CollectionUtil.isNotEmpty(entry)) {
            Iterator iterator = entry.iterator();
            while (iterator.hasNext()) {
                SVNDirEntry svnDirEntry = (SVNDirEntry) iterator.next();
                String repositoryName = svnDirEntry.getRepositoryRoot().getPath().substring(1);//仓库名
                String path = svnDirEntry.getURL().getPath().substring(svnDirEntry.getRepositoryRoot().getPath().length());//文件路径
                SvnFileDirectoryStructure sonStructure = new SvnFileDirectoryStructure(svnDirEntry.getName(), svnDirEntry.getKind().getID(), svnDirEntry.getAuthor(), svnDirEntry.getDate(), repositoryName, path, svnDirEntry.getURL().toString(), accessProtocol, svnDirEntry.getURL().getHost(), String.valueOf(svnDirEntry.getURL().getPort()), repositoryUrl, username, password);
                sonStructure.setChildren(new LinkedList<>());
                if (StringUtils.isNotEmpty(fileNameSearch)) {
                    if (svnDirEntry.getName().contains(fileNameSearch)) {
                        structure.addChild(sonStructure);
                    }
                } else {
                    //控制只显示文件夹
                    if (svnDirEntry.getKind() == SVNNodeKind.DIR) {
                        structure.addChild(sonStructure);
                    }
                }
           /* if (svnDirEntry.getKind() == SVNNodeKind.DIR) {
                sonStructure.setChildren(new LinkedList<>());
                String tempPath = (filePath.equals("") ? svnDirEntry.getName() : filePath + "/" + svnDirEntry.getName());
                listEntry(sonStructure, repository, tempPath, fileNameSearch);
            }*/
            }
        }
    }

    /**
     * 
    * @Title: listSvnDirEntry
    * @Description: 递归获取每一层目录
    * @author author
    * @param result
    * @param repository svb仓库
    * @param path  路径
    * @param fileNameSearch 查找的文件名
    * @throws SVNException
     */
    private static void listSvnDirEntry(List<SVNDirEntry> result, SVNRepository repository, String path, String fileNameSearch) throws SVNException {
        Collection entry = repository.getDir(path, -1, null, (Collection) null);
        if (CollectionUtil.isNotEmpty(entry)) {
            Iterator iterator = entry.iterator();
            while (iterator.hasNext()) {
                SVNDirEntry svnDirEntry = (SVNDirEntry) iterator.next();
                if (StringUtils.isNotEmpty(fileNameSearch)) {
                    if (svnDirEntry.getName().contains(fileNameSearch)) {
                        result.add(svnDirEntry);
                    }
                } else {
                    result.add(svnDirEntry);
                }
                if (svnDirEntry.getKind() == SVNNodeKind.DIR) {
                    String tempPath = (path.equals("") ? svnDirEntry.getName() : path + "/" + svnDirEntry.getName());
                    listSvnDirEntry(result, repository, tempPath, fileNameSearch);
                }
            }
        }
    }


    /**
     * 
    * @Title: listEntry
    * @Description: 罗列svn文件
    * @author author
    * @param files
    * @param repository
    * @param repositoryUrl 文件路径
    * @param path
    * @param revision void
     */
    private static void listEntry(List<String> files, SVNRepository repository, String repositoryUrl, String path, Long revision) {
        try {
            Collection entry = repository.getDir(path, revision, null, (Collection) null);
            if (CollectionUtil.isNotEmpty(entry)) {
                Iterator iterator = entry.iterator();
                while (iterator.hasNext()) {
                    SVNDirEntry svnDirEntry = (SVNDirEntry) iterator.next();
                    if (svnDirEntry.getKind() == SVNNodeKind.FILE) {
                        String filePath = svnDirEntry.getURL().toString().split(repositoryUrl, 2)[1];//文件路径
                        files.add(filePath);
                    } else if (svnDirEntry.getKind() == SVNNodeKind.DIR) {
                        String dirPath = svnDirEntry.getURL().toString().split(repositoryUrl, 2)[1];//文件夹路径
                        listEntry(files, repository, repositoryUrl, dirPath, revision);
                    }
                }
            }
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
        }
    }


    /**
     * 
    * @Title: getFilesUnderDir
    * @Description: 获取snv目录下的文件
    * @author author
    * @param repositoryUrl 仓库Url
    * @param username
    * @param password
    * @param dirPath 目录
    * @param revision 版本
    * @return List<String>
     */
    public static List<String> getFilesUnderDir(String repositoryUrl, String username, String password, String dirPath, Long revision) {
        List<String> files = new LinkedList<>();
        try {
            SVNRepository svnRepository = createRepositoryDriver(repositoryUrl, username, password);
            if (svnRepository == null) {
                return null;
            }
            SVNNodeKind svnNodeKind = svnRepository.checkPath(dirPath, -1);
            //必须为目录
            if (svnNodeKind != SVNNodeKind.DIR) {
                return null;
            }
            listEntry(files, svnRepository, repositoryUrl, dirPath, revision);
            return files;
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return null;
        }
    }

    /**
     * @param repositoryUrl 仓库url
     * @param username
     * @param password
     * @param dirPath       文件夹位置
     * @return java.util.List<java.lang.String>
     * @Description 获取文件夹当前版本下的全部文件
     * @MethodName getFilesUnderDir
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/3/11 11:46
     */
    public static List<String> getFilesUnderDir(String repositoryUrl, String username, String password, String dirPath) {
        return getFilesUnderDir(repositoryUrl, username, password, dirPath, -1L);
    }

    /**
     * @param url
     * @return java.lang.String
     * @Description 获取文件当前版本号
     * @MethodName getCurrentRevision
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/3/11 16:18
     */
    public static String getCurrentCommittedRevision(String url, String username, String password) {
        try {
            SVNClientManager clientManager = setUpSVNClient(username, password);
            SVNURL svnurl = SVNURL.parseURIEncoded(url);
            SVNInfo info = clientManager.getWCClient().doInfo(svnurl, SVNRevision.create(-1L), SVNRevision.create(-1L));
            return String.valueOf(info.getCommittedRevision().getNumber());
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return null;
        }
    }

    /**
     * @param url 仓库url
     * @param Revision
     * @param username
     * @param password
     * @return org.tmatesoft.svn.core.wc.SVNInfo
     * @Description 获取文件url下某版本的svn信息
     * @MethodName getSvnInfo
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/6/27 11:09
     */
    public static SVNInfo getSvnInfo(String url, Long Revision, String username, String password) {
        try {
            SVNClientManager clientManager = setUpSVNClient(username, password);
            SVNURL svnurl = SVNURL.parseURIEncoded(url);
            SVNInfo info = clientManager.getWCClient().doInfo(svnurl, SVNRevision.create(Revision), SVNRevision.create(Revision));
            return info;
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return null;
        }
    }

    /**
     * @param repository
     * @return java.util.List<cn.pioneeruniverse.dev.entity.SvnCommitLog>
     * @Description 获取文件提交历史记录
     * @MethodName getLog
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/10/30 13:55
     */
    public static List<SvnCommitLog> getLog(SVNRepository repository) {
        List<SvnCommitLog> commitLogList = new LinkedList<SvnCommitLog>();
        Long startRevision = 0L;
        Long endRevision = -1L;
        Collection log = null;
        try {
            log = repository.log(new String[]{""}, null, startRevision, endRevision, true, true);
            if (log != null) {
                for (Iterator entries = log.iterator(); entries.hasNext(); ) {
                    SVNLogEntry logEntry = (SVNLogEntry) entries.next();
                    SvnCommitLog commitLog = new SvnCommitLog();
                    commitLog.setRevision(logEntry.getRevision());
                    commitLog.setAuthor(logEntry.getAuthor());
                    commitLog.setCommitDate(logEntry.getDate());
                    commitLog.setCommitMessage(logEntry.getMessage());
                    if (logEntry.getChangedPaths().size() > 0) {
                        List<ChangedPath> changedPathList = new LinkedList<ChangedPath>();
                        Set changedPathsSet = logEntry.getChangedPaths().keySet();
                        for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext(); ) {
                            SVNLogEntryPath entryPath = logEntry.getChangedPaths().get(changedPaths.next());
                            ChangedPath changedPath = new ChangedPath();
                            changedPath.setType(String.valueOf(entryPath.getType()));
                            changedPath.setPath(entryPath.getPath());
                            if (entryPath.getCopyPath() != null) {
                                StringBuilder msg = new StringBuilder("from").
                                        append(entryPath.getCopyPath()).
                                        append("revision").
                                        append(entryPath.getCopyRevision());
                                changedPath.setCopyPathMsg(msg.toString());
                            }
                            changedPathList.add(changedPath);
                        }
                        commitLog.setChangedPathList(changedPathList);
                    }
                    commitLogList.add(commitLog);
                }
            }
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return null;
        }
        return commitLogList;
    }

    /**
     * 
    * @Title: getCurrentRevision
    * @Description: 获取最新版本
    * @author author
    * @param repositoryUrl 仓库url
    * @param filePath 文件路径
    * @param username
    * @param password
    * @return String
     */
    public static String getCurrentRevision(String repositoryUrl, String filePath, String username, String password) {
        String revision = null;
        try {
            SVNRepository svnRepository = createRepositoryDriver(repositoryUrl, username, password);
            if (svnRepository == null) {
                return null;
            }
            Collection result = svnRepository.log(new String[]{filePath}, null, 0L, -1L, false, false);
            if (result != null) {
                SVNLogEntry logEntry = (SVNLogEntry) Arrays.asList(result.toArray()).get(result.size() - 1);
                revision = String.valueOf(logEntry.getRevision());
            }
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
        }
        return revision;
    }

    /**
     * @param repositoryUrl 仓库url
     * @param filePath
     * @param revision
     * @param username
     * @param password
     * @return java.lang.String
     * @Description 获取某个版本下文件内容详情
     * @MethodName checkoutFileToString
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/1/29 10:07
     */
    public static String checkoutFileToString(String repositoryUrl, String filePath, Long revision, String charset, String username, String password) {
        try {
            SVNRepository svnRepository = createRepositoryDriver(repositoryUrl, username, password);
            if (svnRepository == null) {
                return null;
            }
            //获得版本库中文件的类型状态（是否存在、是目录还是文件）
            SVNNodeKind nodeKind = svnRepository.checkPath(filePath, revision);
            if (nodeKind != SVNNodeKind.FILE) {
                return null;
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            SVNProperties properties = new SVNProperties();
            svnRepository.getFile(filePath, revision, properties, outputStream);
            String doc = new String(outputStream.toByteArray(), Charset.forName(charset));
            return doc;
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return null;
        }
    }

    /**
     * @param url 仓库url
     * @param username
     * @param password
     * @return boolean
     * @Description 判断一个url在svn上是否存在
     * @MethodName isUrlExist
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/10/30 13:57
     */
    public static boolean isUrlExist(String url, String username, String password) {
        try {
            SVNNodeKind nodeKind = createRepositoryDriver(url, username, password).checkPath("", -1);
            return nodeKind == SVNNodeKind.NONE || nodeKind == SVNNodeKind.UNKNOWN;
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return false;
        }
    }

    /**
     * @param repositoryUrl 仓库url
     * @param filePath
     * @param username
     * @param password
     * @return boolean
     * @Description 通过文件url判断是否是文件夹
     * @MethodName isUrlDir
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/5/6 13:57
     */
    public static boolean isUrlDir(String repositoryUrl, String filePath, String username, String password) throws SVNException {
        SVNNodeKind nodeKind = createRepositoryDriver(repositoryUrl, username, password).checkPath(filePath, -1);
        return nodeKind == SVNNodeKind.DIR;
    }


    /**
     * 
    * @Title: doMerge
    * @Description: 合并
    * @author author
    * @param source1Url 合并对象1
    * @param source1Revision
    * @param source2Url 合并对象2
    * @param source2Revision
    * @param targetWCPath
     */
    @Deprecated
    public static void doMerge(String source1Url, Long source1Revision, String source2Url, Long source2Revision, String targetWCPath) {
        try {
            SVNClientManager clientManager = setUpSVNClient("admin", "123456");
            SVNDiffClient diffClient = clientManager.getDiffClient();
           /* diffClient.setIgnoreExternals(false);
            DefaultSVNOptions options = (DefaultSVNOptions) diffClient.getOptions();
            options.setConflictHandler(new ConflictResolverHandler());
            diffClient.setOptions(options);*/
            diffClient.doMerge(SVNURL.parseURIEncoded(source1Url),
                    SVNRevision.create(source1Revision),
                    SVNURL.parseURIEncoded(source2Url),
                    SVNRevision.create(source2Revision),
                    new File(targetWCPath),
                    SVNDepth.INFINITY,
                    false, false, false, false);
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    /**
     * @deprecated
    * @Title: doDiff
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @author author
    * @param sourceUrl
    * @param revision1
    * @param revision2
    * @return String
     */
    public static String doDiff(String sourceUrl, Long revision1, Long revision2) {
        try {
            SVNClientManager clientManager = setUpSVNClient("admin", "123456");
            SVNDiffClient diffClient = clientManager.getDiffClient();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            diffClient.doDiff(SVNURL.parseURIEncoded(sourceUrl),
                    SVNRevision.create(revision1),
                    SVNURL.parseURIEncoded(sourceUrl),
                    SVNRevision.create(revision2),
                    true,
                    false,
                    outputStream
            );
            String doc = new String(outputStream.toByteArray(), Charset.forName("UTF-8"));
            return doc;
        } catch (SVNException e) {
            logger.error(String.valueOf(e.getErrorMessage()), e);
            return null;
        }
    }


    /**
     * 
    * @Title: getUser
    * @Description: 获取SVN用户列表
    * @author author
    * @param ip
    * @return List<String>
     */
    public static List<String> getUser(String ip) {
        try {
            return subversionWbsHelper.getSvnAllUser(subversionWbsHelper.createClient(ip));
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            return null;
        }
    }

    /**
     * 
    * @Title: getUserAuth
    * @Description: 获取用户svn权限
    * @author author
    * @param ip
    * @param repositoryName
    * @param path
    * @return
    * @throws Exception Map<String,String>
     */
    public static Map<String, String> getUserAuth(String ip, String repositoryName, String path) throws Exception {
        try {
            return subversionWbsHelper.getSvnAllUserAuth(subversionWbsHelper.createClient(ip), repositoryName, path);
        } catch (Exception e) {
            logger.error(String.valueOf(e.getMessage()), e);
            throw new Exception(e);
        }
    }

    /**
     * 
    * @Title: modifyConfigFile
    * @Description: 修改svn配置文件
    * @author author
    * @param accessProtocol
    * @param ip
    * @param repositoryName
    * @param path
    * @param modifyOperates
    * @return
    * @throws Exception String
     */
    public static String modifyConfigFile(Integer accessProtocol, String ip, String repositoryName, String path, String modifyOperates) throws Exception {
        return subversionWbsHelper.modifySvnConfigFile(subversionWbsHelper.createClient(ip), accessProtocol, repositoryName, path, modifyOperates);
    }

    public static void modifySvnServeConfgFileForRepository(String ip, String repositoryName) throws Exception {
        subversionWbsHelper.modifySvnserveConfgFileForRepository(subversionWbsHelper.createClient(ip), repositoryName);
    }

    /**
     * 
    * @Title: modifySvnUserPassword
    * @Description: 修改svn用户密码
    * @author author
    * @param accessProtocol
    * @param ip
    * @param userMap 用户信息
    * @throws Exception
     */
    public static void modifySvnUserPassword(Integer accessProtocol, String ip, Map<String, String> userMap) throws Exception {
        subversionWbsHelper.modifySvnUserPassword(subversionWbsHelper.createClient(ip), accessProtocol, userMap);
    }

    /**
     * 
    * @Title: createSvnRepository
    * @Description: 创建svn仓库
    * @author author
    * @param ip
    * @param repositoryName 仓库名
    * @throws Exception
     */
    public static void createSvnRepository(String ip, String repositoryName) throws Exception {
        subversionWbsHelper.createSvnRepository(subversionWbsHelper.createClient(ip), repositoryName);
    }

}
