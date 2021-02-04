package com.szcinda.express;

import com.szcinda.express.dto.MainDto;
import com.szcinda.express.persistence.BugType;
import com.szcinda.express.persistence.DesignerBug;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    private final DesignerBugService designerBugService;

    public HomeServiceImpl(DesignerBugService designerBugService) {
        this.designerBugService = designerBugService;
    }

    @Override
    public MainDto query(String userId) {
        MainDto mainDto = new MainDto();
        // 设计器
        List<DesignerBug> countByUNRESOLVED = designerBugService.countByUNRESOLVED(BugType.DESIGNER);
        long EMERGENCYCount = countByUNRESOLVED.stream().filter(designerBug -> designerBug.getBugLevel().equals("紧急")).count();
        mainDto.setDesignerBugCount(countByUNRESOLVED.size());
        mainDto.setDesignerBugCountEmergency(EMERGENCYCount);

        List<DesignerBug> countMyJobByUNRESOLVED = designerBugService.countMyJobByUNRESOLVED(userId);
        long myEMERGENCYCount = countMyJobByUNRESOLVED.stream().filter(designerBug -> designerBug.getBugLevel().equals("紧急")).count();
        long myCountDesigner = countMyJobByUNRESOLVED.stream().filter(designerBug -> designerBug.getType().equals(BugType.DESIGNER)).count();
        long myCountExecutor = countMyJobByUNRESOLVED.stream().filter(designerBug -> designerBug.getType().equals(BugType.EXECUTOR)).count();
        long myCountOC = countMyJobByUNRESOLVED.stream().filter(designerBug -> designerBug.getType().equals(BugType.OC)).count();
        mainDto.setMyBugCount(countMyJobByUNRESOLVED.size());
        mainDto.setMyBugCountEmergency(myEMERGENCYCount);
        mainDto.setMyBugCountDesigner(myCountDesigner);
        mainDto.setMyBugCountExecutor(myCountExecutor);
        mainDto.setMyBugCountOc(myCountOC);

        List<DesignerBug> countByWeekFinished = designerBugService.countByWeekFinished(BugType.DESIGNER);
        myEMERGENCYCount = countByWeekFinished.stream().filter(designerBug -> designerBug.getBugLevel().equals("紧急")).count();
        mainDto.setDesignerWeekSolvedCount(countByWeekFinished.size());
        mainDto.setDesignerWeekSolvedCountEmergency(myEMERGENCYCount);


        List<DesignerBug> countMyJobByWeekFinished = designerBugService.countMyJobByWeekFinished(userId);
        myEMERGENCYCount = countMyJobByWeekFinished.stream().filter(designerBug -> designerBug.getBugLevel().equals("紧急")).count();
        myCountDesigner = countMyJobByWeekFinished.stream().filter(designerBug -> designerBug.getType().equals(BugType.DESIGNER)).count();
        myCountExecutor = countMyJobByWeekFinished.stream().filter(designerBug -> designerBug.getType().equals(BugType.EXECUTOR)).count();
        myCountOC = countMyJobByWeekFinished.stream().filter(designerBug -> designerBug.getType().equals(BugType.OC)).count();
        mainDto.setMyBugWeekFinishedCount(countMyJobByWeekFinished.size());
        mainDto.setMyBugWeekFinishedCountEmergency(myEMERGENCYCount);
        mainDto.setMyBugWeekFinishedCountDesigner(myCountDesigner);
        mainDto.setMyBugWeekFinishedCountExecutor(myCountExecutor);
        mainDto.setMyBugWeekFinishedCountOc(myCountOC);

        // 执行器
        countByUNRESOLVED = designerBugService.countByUNRESOLVED(BugType.EXECUTOR);
        EMERGENCYCount = countByUNRESOLVED.stream().filter(designerBug -> designerBug.getBugLevel().equals("紧急")).count();
        countByWeekFinished = designerBugService.countByWeekFinished(BugType.EXECUTOR);
        myEMERGENCYCount = countByWeekFinished.stream().filter(designerBug -> designerBug.getBugLevel().equals("紧急")).count();
        mainDto.setExecutorBugCount(countByUNRESOLVED.size());
        mainDto.setExecutorBugCountEmergency(EMERGENCYCount);
        mainDto.setExecutorWeekSolvedCount(countByWeekFinished.size());
        mainDto.setExecutorWeekSolvedCountEmergency(myEMERGENCYCount);
        // OC
        countByUNRESOLVED = designerBugService.countByUNRESOLVED(BugType.OC);
        EMERGENCYCount = countByUNRESOLVED.stream().filter(designerBug -> designerBug.getBugLevel().equals("紧急")).count();
        countByWeekFinished = designerBugService.countByWeekFinished(BugType.OC);
        myEMERGENCYCount = countByWeekFinished.stream().filter(designerBug -> designerBug.getBugLevel().equals("紧急")).count();
        mainDto.setOcBugCount(countByUNRESOLVED.size());
        mainDto.setOcBugCountEmergency(EMERGENCYCount);
        mainDto.setOcWeekSolvedCount(countByWeekFinished.size());
        mainDto.setOcWeekSolvedCountEmergency(myEMERGENCYCount);
        return mainDto;
    }
}
