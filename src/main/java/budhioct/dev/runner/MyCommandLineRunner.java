package budhioct.dev.runner;

import budhioct.dev.entity.*;
import budhioct.dev.repository.*;
import budhioct.dev.utilities.Names;
import budhioct.dev.utilities.Ownership;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {

    private final StockRepository stockRepository;
    private final LogStockRepository logStockRepository;
    private final StakeholderRepository stakeholderRepository;
    private final OfficialAgentRepository officialAgentRepository;
    private final SubAgentRepository subAgentRepository;
    private final DistributionRepository distributionRepository;
    private final TransactionRepository transactionRepository;
    private final FolksyRepository folksyRepository;
    private final Faker faker = new Faker(new Locale("in-ID"));
    private final Random random = new Random();

    private List<Folksy> folksies = new ArrayList<>();
    private long officialAgentOwnerId = 1L;
    private long subAgentOwnerId = 10000L;

    @Override
    public void run(String... args) throws Exception {
        clearDatabase();

        List<Stakeholder> stakeholders = createStakeholder(10);
        stakeholderRepository.saveAll(stakeholders);

        List<OfficialAgent> officialAgents = createOfficialAgents(stakeholders);
        officialAgentRepository.saveAll(officialAgents);

        //List<SubAgent> subAgents = new ArrayList<>();
        //for (OfficialAgent officialAgent : officialAgents) {
        //    subAgents.addAll(createSubAgents(officialAgent, 10));
        //}

        List<SubAgent> subAgents = createSubAgents(officialAgents, 10);
        subAgentRepository.saveAll(subAgents);

        saveFolksies();

        System.out.println("Data successfully injected into the database.");
    }

    private void clearDatabase() {
        folksyRepository.deleteAll();
        transactionRepository.deleteAll();
        distributionRepository.deleteAll();
        subAgentRepository.deleteAll();
        officialAgentRepository.deleteAll();
        stakeholderRepository.deleteAll();
        logStockRepository.deleteAll();
        stockRepository.deleteAll();

        officialAgentOwnerId = 1L;
        subAgentOwnerId = 10000L;
    }

    private List<Stakeholder> createStakeholder(int count) {
        List<Stakeholder> stakeholders = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Stock stakeholderStock = new Stock(null, Ownership.SUBHOLDING, 30000);
            Stakeholder stakeholder = new Stakeholder(
                    "PT " + faker.rockBand().name(),
                    faker.address().streetAddress(),
                    "+628" + faker.numerify("#####") + "xxxxx",
                    new ArrayList<>(),
                    stakeholderStock
            );
            stakeholders.add(stakeholder);
        }
        return stakeholders;
    }

    private List<OfficialAgent> createOfficialAgents(List<Stakeholder> stakeholders) {
        List<OfficialAgent> officialAgents = new ArrayList<>();

        for (Stakeholder stakeholder : stakeholders) {
            for (int i = 1; i <= 10; i++) {
                Stock officialStock = new Stock(officialAgentOwnerId++, Ownership.OFFICIAL_AGENT, 1000);
                OfficialAgent officialAgent = new OfficialAgent(
                        Names.agenResmi.get(random.nextInt(Names.agenResmi.size())),
                        faker.address().streetAddress(),
                        stakeholder,
                        officialStock
                );
                officialAgents.add(officialAgent);
            }
        }
        return officialAgents;
    }

    private List<SubAgent> createSubAgents(OfficialAgent officialAgent, int count) {
        List<SubAgent> subAgents = new ArrayList<>();
        long ownerId = officialAgent.getStock().getId() + 1;
        for (int i = 1; i <= count; i++) {
            subAgents.add(new SubAgent(
                    Names.agenResmi.get(random.nextInt(Names.agenResmi.size())),
                    faker.address().streetAddress(),
                    officialAgent,
                    new Stock(ownerId++, Ownership.SUB_AGENT, 20)
            ));
        }
        return subAgents;
    }

    private List<SubAgent> createSubAgents(List<OfficialAgent> officialAgents, int count) {
        List<SubAgent> subAgents = new ArrayList<>();

        for (OfficialAgent officialAgent : officialAgents) {
            for (int i = 1; i <= count; i++) {
                Stock subAgentStock = new Stock(subAgentOwnerId++, Ownership.SUB_AGENT, 20);
                SubAgent subAgent = new SubAgent(
                        Names.warungKelontong.get(random.nextInt(Names.warungKelontong.size())),
                        faker.address().streetAddress(),
                        officialAgent,
                        subAgentStock
                );
                subAgents.add(subAgent);
            }
        }

        return subAgents;
    }

    private void saveFolksies() {
        for (int i = 0; i < 100; i++) {
            folksies.add(new Folksy(faker.name().fullName()));
        }
        folksyRepository.saveAll(folksies);
    }
}
