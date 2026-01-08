package store.global.config;

public final class DIConfig {

    private final CrewRepository crewRepository = new CrewRepository();
    private final MissionRepository missionRepository = new MissionRepository();
    private final MatchRepository matchRepository = new MatchRepository();

    public PairMatchingController pairMatchingController() {
        return new PairMatchingController(
                fileService(),
                initialService(),
                inputView(),
                pairService(),
                outputView()
        );
    }

    public FileService fileService() {
        return new FileService(crewRepository());
    }

    public InitialService initialService() {
        return new InitialService(missionRepository());
    }

    public PairService pairService() {
        return new PairService(
                missionRepository(),
                crewRepository(),
                matchRepository()
        );
    }

    public InputView inputView() {
        return new InputView();
    }

    public OutputView outputView() {
        return new OutputView();
    }

    public CrewRepository crewRepository() {
        return crewRepository;
    }

    public MissionRepository missionRepository() {
        return missionRepository;
    }

    public MatchRepository matchRepository() {
        return matchRepository;
    }
}
