class CreateMinecraftAccounts < ActiveRecord::Migration
  def change
    create_table :minecraft_accounts do |t|
      t.string :username, index: true
      t.string :password
      t.string :user_type
      t.string :state, index: true
      t.integer :allocated_user_id, index: true

      t.timestamps
    end
  end
end
