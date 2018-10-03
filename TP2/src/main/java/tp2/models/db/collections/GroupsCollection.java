package tp2.models.db.collections;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import tp2.models.db.documents.GroupModel;
import tp2.models.db.internals.BaseCollection;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static tp2.models.db.collections.Accessors.getClientsCollection;

@SuppressWarnings("unused")
public class GroupsCollection extends BaseCollection<GroupModel> {
	GroupsCollection() {
		super(GroupModel.class);
		Flowable.defer(() -> Flowable.fromIterable(list()))
		        .subscribeOn(Schedulers.computation())
				.parallel()
				.runOn(Schedulers.io())
		        .doOnNext(group -> group.getMembers()
		                                .removeIf(member -> getClientsCollection().get(member) == null))
				.doOnNext(group -> {
					if (group.getMembers().size() > 0) save(group);
					else delete(group);
				})
				.sequential()
		        .repeatWhen(completed -> completed.delay(5, TimeUnit.SECONDS))
				.subscribe();
	}
	
	public GroupModel save(@NotNull GroupModel group) {
		return super.save(group);
	}
	
	public List<GroupModel> list() {
		return query().asList();
	}
	
	public boolean delete(GroupModel group) {
		return getDatastore().delete(group).wasAcknowledged();
	}
}