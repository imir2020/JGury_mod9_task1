package by.javagur.spring.database.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserImage is a Querydsl query type for UserImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserImage extends EntityPathBase<UserImage> {

    private static final long serialVersionUID = -309077094L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserImage userImage = new QUserImage("userImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final QUser user;

    public QUserImage(String variable) {
        this(UserImage.class, forVariable(variable), INITS);
    }

    public QUserImage(Path<? extends UserImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserImage(PathMetadata metadata, PathInits inits) {
        this(UserImage.class, metadata, inits);
    }

    public QUserImage(Class<? extends UserImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

