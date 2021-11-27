package org.example.BeerMachine.service;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.example.BeerMachine.BeerMachineCommunication.MachineConnection;
import org.example.BeerMachine.BeerMachineCommunication.Read;
import org.example.BeerMachine.BeerMachineCommunication.Subscription;
import org.example.BeerMachine.BeerMachineCommunication.Write;
import org.example.BeerMachine.BeerMachineController;
import org.example.BeerMachine.data.models.BatchReport;
import org.example.BeerMachine.data.models.MachineState;
import org.example.BeerMachine.data.payloads.response.MessageResponse;
import org.example.BeerMachine.data.repository.BatchReportRepository;
import org.example.BeerMachine.data.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MachineServiceImpl implements MachineService {
    private final MachineConnection machineConnection = new MachineConnection();
    private final Write write = new Write();
    private final Read read = new Read();
    private final MachineState machineState = BeerMachineController.getBeerMachineController().getMachineState();
    private final Map<String, Subscription> ingredients = machineState.getIngredients();
    private final String barley = "Barley", wheat = "Wheat", hops = "Hops", malt = "Malt", yeast = "Yeast";

    @Autowired
    BatchReportRepository batchReportRepository;
    @Autowired
    BatchRepository batchRepository;

    @Override
    public MessageResponse resetMachine() {
        write.reset();
        return new MessageResponse("Machine reset...");
    }
    @Override
    public MessageResponse startMachine(Integer batchId) {
        try {
            if(read.checkState() != 4) {
                write.reset();
            }
            BatchReport batchReport = batchReportRepository.findById(batchId).get();
            //The subtraction of 1 from the type_id is used because of different indexing methods (0index!=1index)
            write.startBatch(batchReport.getBatchId(), batchReport.getSpeed(), batchReport.getType().getId()-1, batchReport.getAmount());
        } catch (Exception e) {
            System.out.println(e);
            return new MessageResponse("Machine didn't start...");
        }
        return new MessageResponse("Machine started...");
    }
    @Override
    public MessageResponse startQueue() {
        MessageResponse response = new MessageResponse("Queue didn't start...");
        /*try {
            ArrayList<Batch> batchQueue = new ArrayList<>(batchRepository.findOrderByQueueSpotAndQueueSpotNotNull());
            if (BeerMachineController.getBeerMachineController().getMachineState().getState() == State.IDLE) {
                while (batchQueue.size() > 0) {
                    Integer firstQueue = batchQueue.get(0).getId();
                    response = startMachine(firstQueue);
                    batchQueue.remove(0);
                }
            } else {
                response = new MessageResponse("Machine not in idle...");
            }
        } catch (Exception e) {
            System.out.println(e);
            response = new MessageResponse("Queue failed...");
        }*/
        return response;
    }

    @Override
    public MessageResponse stopMachine() {
        write.stop();
        return new MessageResponse("Machine stopped...");
    }
    @Override
    public MessageResponse abortMachine() {
        write.abort();
        return new MessageResponse("Machine aborted...");
    }
    @Override
    public MessageResponse clearMachine() {
        write.clear();
        return new MessageResponse("Machine cleared...");
    }
    @Override
    public MessageResponse getState() {
        int message = read.checkState();
        System.out.println(message);
        return new MessageResponse("Machine state: " + message);
    }

    @Override
    public UShort getAmountToProduce() {
        return read.getAmountToProduce();
    }

    @Override
    public UShort getBatchId() {
        return read.getBatchId();
    }

    @Override
    public float getSpeed() {
        return read.getSpeed();
    }

    @Override
    public float getBarley() {
        if (!ingredients.get(barley).isAlive()) {
            ingredients.get(barley).start();
        }
        return ingredients.get(barley).getBarley();
    }
    @Override
    public float getHops() {
        if (!ingredients.get(hops).isAlive()) {
            ingredients.get(hops).start();
        }
        return ingredients.get(hops).getHops();
    }
    @Override
    public float getMalt() {
        if (!ingredients.get(malt).isAlive()) {
            ingredients.get(malt).start();
        }
        return ingredients.get(malt).getMalt();
    }
    @Override
    public float getWheat() {
        if (!ingredients.get(wheat).isAlive()) {
            ingredients.get(wheat).start();
        }
        return ingredients.get(wheat).getWheat();
    }
    @Override
    public float getYeast() {
        if (!ingredients.get(yeast).isAlive()) {
            ingredients.get(yeast).start();
        }
        return ingredients.get(yeast).getYeast();
    }
    @Override
    public float getHumidity() {
        if (!machineState.getHumiditySub().isAlive()) {
            machineState.getHumiditySub().start();
        }
        return machineState.getHumiditySub().getHumidity();
    }
    @Override
    public float getTemperature() {
        if (!machineState.getTemperatureSub().isAlive()) {
            machineState.getTemperatureSub().start();
        }
        return machineState.getTemperatureSub().getTemperature();
    }
    @Override
    public float getVibrations() {
        if (!machineState.getVibrationSub().isAlive()) {
            machineState.getVibrationSub().start();
        }
        return machineState.getVibrationSub().getVibrations();
    }
    @Override
    public int getStopReason() {
        if (!machineState.getStopReasonSub().isAlive()) {
            machineState.getStopReasonSub().start();
        }
        return machineState.getStopReasonSub().getStopReason();
    }

    @Override
    public UShort getTotalCount() {
        if (!machineState.getTotalCountSub().isAlive()) {
            machineState.getTotalCountSub().start();
        }
        return machineState.getTotalCountSub().getTotalCount();
    }

    @Override
    public UShort getGoodCount() {
        if (!machineState.getGoodCountSub().isAlive()) {
            machineState.getGoodCountSub().start();
        }
        return machineState.getGoodCountSub().getGoodCount();
    }

    @Override
    public UShort getBadCount() {
        if (!machineState.getBadCountSub().isAlive()) {
            machineState.getBadCountSub().start();
        }
        return machineState.getBadCountSub().getBadCount();
    }

    @Override
    public UShort getMaintenanceCount() {
        if (!machineState.getMaintenanceSub().isAlive()) {
            machineState.getMaintenanceSub().start();
        }
        return machineState.getMaintenanceSub().getMaintenance();
    }




    @Override
    public MessageResponse setHost(String host) {
        machineConnection.setHost(host);
        return new MessageResponse("Host changed");
    }

    @Override
    public MessageResponse getHost(){
        return new MessageResponse("The host is: " + machineConnection.getHost());
    }


}
