class CreateMinecraftAccounts < ActiveRecord::Migration
  def change
    create_table :minecraft_accounts do |t|
      t.string :username, index: true
      t.string :password
      t.string :state, index: true

      t.timestamps
    end
  end
end
