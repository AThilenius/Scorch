package com.thilenius.flame.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages different transaction chains, loading and unloading as needed
 */
public class TransactionForest {

    private HashMap<String, List<Statement>> m_activeChains = new HashMap<String, List<Statement>>();

    public void Handle (Transaction transaction) {
        if (transaction.Statements == null  || transaction.Statements.size() == 0) {
            System.out.println("No Statements were provided with the transaction. Terminating.");
            return;
        }

        // Check if the root of the chain is already loaded
        String rootPath = transaction.Statements.get(0).TPath.trim();
        List<Statement> statementChain = m_activeChains.get(rootPath);
        if (statementChain == null) {
            statementChain = new ArrayList<Statement>();
            m_activeChains.put(rootPath, statementChain);

            // Activate the first node
            Statement rootStatement = Statement.getSubclass(transaction.Statements.get(0));
            if (rootStatement == null) {
                System.out.println("Failed to parse transaction statement: " + rootPath + "! Terminating.");
                return;
            }
            statementChain.add(rootStatement);
            rootStatement.Execute();
        }

        // For each child node after the root
        for (int i = 1; i < transaction.Statements.size(); i++) {
            Statement childStatement = transaction.Statements.get(i);

            // At the end of the chain? Load it without any unloads
            if (statementChain.size() <= i) {

            }

            if (statementChain.size() <= i || !statementChain.get(i).TPath.equals(childStatement.TPath)) {

            }
        }
    }

}
