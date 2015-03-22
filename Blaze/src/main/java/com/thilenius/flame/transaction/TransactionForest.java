package com.thilenius.flame.transaction;

import com.thilenius.flame.statement.StatementBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages different transaction chains, loading and unloading as needed
 */
public class TransactionForest {

    private HashMap<String, List<StatementBase>> m_activeChains = new HashMap<String, List<StatementBase>>();

    public void Handle (Transaction transaction) {
        if (transaction.statementBases == null  || transaction.statementBases.size() == 0) {
            System.out.println("No Statements were provided with the transaction. Terminating.");
            return;
        }

        // Check if the root of the chain is already loaded
        String rootPath = transaction.statementBases.get(0).TPath.trim();
        List<StatementBase> statementBaseChain = m_activeChains.get(rootPath);
        if (statementBaseChain == null) {
            statementBaseChain = new ArrayList<StatementBase>();
            m_activeChains.put(rootPath, statementBaseChain);

            // Activate the first node
            StatementBase rootStatementBase = StatementBase.getSubclass(transaction.statementBases.get(0));
            if (rootStatementBase == null) {
                System.out.println("Failed to parse transaction statement: " + rootPath + "! Terminating.");
                return;
            }
            statementBaseChain.add(rootStatementBase);
            rootStatementBase.Execute();
        }

        // For each child node after the root
        for (int i = 1; i < transaction.statementBases.size(); i++) {
            StatementBase childStatementBase = transaction.statementBases.get(i);

            // At the end of the chain? Load it without any unloads
            if (statementBaseChain.size() <= i) {

            }

            if (statementBaseChain.size() <= i || !statementBaseChain.get(i).TPath.equals(childStatementBase.TPath)) {

            }
        }
    }

}
