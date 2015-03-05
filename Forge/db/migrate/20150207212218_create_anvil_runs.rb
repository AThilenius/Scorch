class CreateAnvilRuns < ActiveRecord::Migration
  def change
    create_table :anvil_runs do |t|
      t.references :user,           index: true
      t.string :calling_ip,         index: true
      t.integer :awarded_points,    index: true
      t.integer :blaze_call_count,  index: true
      t.binary :calling_code
      t.binary :api_calls
      t.timestamps
    end
  end
end
